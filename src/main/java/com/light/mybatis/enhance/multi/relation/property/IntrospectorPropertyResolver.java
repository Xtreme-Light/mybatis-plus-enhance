package com.light.mybatis.enhance.multi.relation.property;

import com.light.mybatis.enhance.multi.relation.metadata.Property;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

public class IntrospectorPropertyResolver extends PropertyResolver {

  private final boolean includeTransientFields;

  public IntrospectorPropertyResolver(boolean includePublicFields) {
    this(includePublicFields, true);
  }

  public IntrospectorPropertyResolver(boolean includePublicFields, boolean includeTransientFields) {
    super(includePublicFields);
    this.includeTransientFields = includeTransientFields;
  }

  @Override
  public void collectProperties(Class<?> type, Map<String, Property> properties) {
    try {
      final BeanInfo beanInfo = Introspector.getBeanInfo(type);
      final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
        try {
          Method readMethod = getReadMethod(propertyDescriptor, type);
          if (!includeTransientFields && isTransient(readMethod)) {
            continue;
          }
          Method writeMethod = getWriteMethod(propertyDescriptor, type, null);
          Property property =
              processProperty(propertyDescriptor.getName(), propertyDescriptor.getPropertyType(),
                  readMethod, writeMethod,
                  properties);
          postProcessProperty(property, propertyDescriptor, readMethod, writeMethod, type,
              properties);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    } catch (IntrospectionException e) {
      throw new RuntimeException(e);
    }
  }

  private void postProcessProperty(Property property, PropertyDescriptor pd, Method readMethod,
      Method writeMethod, Class<?> type,
      Map<String, Property> properties) {

    if (writeMethod == null && property != null) {
      writeMethod = getWriteMethod(pd, type, property.getType());
      if (writeMethod != null) {
        processProperty(property.getName(), property.getType(), readMethod, writeMethod,
            properties);
      }
    }
  }

  /**
   * Processes a property, adding it to the map of properties for the owning type being processed
   *
   * @param propertyName the name of the property
   * @param propertyType the Class of the property
   * @param readMethod   the read method for the property
   * @param writeMethod  the write method for the property
   * @param properties
   */
  protected Property processProperty(String propertyName, Class<?> propertyType, Method readMethod,
      Method writeMethod, Map<String, Property> properties) {
    final Property.Builder builder = new Property.Builder();
    Property property = null;
    builder.expression(propertyName);
    builder.name(propertyName);
    if (readMethod != null) {
      builder.getter(readMethod.getName() + "()");
    }
    if (writeMethod != null) {
      builder.setter(writeMethod.getName() + "(%s)");
    }
    if (readMethod != null || writeMethod != null) {
      builder.type(resolvePropertyType(readMethod, propertyType));
      property = builder.build();
      properties.putIfAbsent(propertyName, property);
    }
    return property;

  }

  public Class<?> resolvePropertyType(Method readMethod, Class<?> rawType) {

    return resolveRawPropertyType(rawType, readMethod);
  }

  /**
   * Resolves the raw property type from a property descriptor; if a read
   * method is available, use it to refine the type. The results of
   * pd.getPropertyType() are sometimes inconsistent across platforms.
   *
   * @param rawType
   * @param readMethod
   * @return
   */
  private Class<?> resolveRawPropertyType(Class<?> rawType, Method readMethod) {
    try {
      return (readMethod == null ? rawType : readMethod.getDeclaringClass()
          .getDeclaredMethod(readMethod.getName(), new Class[0])
          .getReturnType());
    } catch (Exception e) {
      return rawType;
    }
  }
  /**
   * Get the read method for the particular property descriptor
   *
   * @param pd the property descriptor
   * @return the property's read method
   */
  private Method getReadMethod(PropertyDescriptor pd, Class<?> type) {
    final String capitalName = capitalize(pd.getName());
    Method readMethod = pd.getReadMethod();

    if (readMethod == null) {
      /*
       * Special handling for older versions of Introspector: if
       * one of the getter or setter is fulfilling a templated type
       * and the other is not, they may not be returned as the same
       * property descriptor
       */
      try {
        readMethod = type.getMethod("get" + capitalName);
      } catch (NoSuchMethodException e) {
        readMethod = null;
      }
    }

    if (readMethod == null && Boolean.class.equals(pd.getPropertyType())) {
      /*
       * Special handling for Boolean "is" read method; not strictly
       * compliant with the JavaBeans specification, but still very common
       */
      try {
        readMethod = type.getMethod("is" + capitalName);
      } catch (NoSuchMethodException e) {
        readMethod = null;
      }
    }

    if (readMethod != null && readMethod.isBridge()) {
      /*
       * Special handling for a bug in sun jdk 1.6.0_u5
       * http://bugs.sun.com/view_bug.do?bug_id=6788525
       */
      readMethod = getNonBridgeAccessor(readMethod);
    }

    return readMethod;
  }

  /**
   * Convert the first character of the provided string to uppercase.
   *
   * @param string
   * @return the String with the first character converter to uppercase.
   */
  protected String capitalize(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  /**
   * Get a real accessor from a bridge method. work around to
   * http://bugs.sun.com/view_bug.do?bug_id=6788525
   *
   * @param bridgeMethod any method that can potentially be a bridge method
   * @return if it is not a problematic method, it is returned back immediately if we can find a
   * non-bridge method with the same name we return that if we cannot find a non-bridge method we
   * return the bridge method back (to prevent any unintended breakage)
   */
  private static Method getNonBridgeAccessor(Method bridgeMethod) {

    Method realMethod = bridgeMethod;
    Method[] otherMethods = bridgeMethod.getDeclaringClass().getMethods();
    for (Method possibleRealMethod : otherMethods) {
      if (possibleRealMethod.getName().equals(bridgeMethod.getName())
          && !possibleRealMethod.isBridge()
          && possibleRealMethod.getParameterTypes().length == 0) {
        realMethod = possibleRealMethod;
        break;
      }
    }
    return realMethod;
  }


  /**
   * Gets the write method for the particular property descriptor
   *
   * @param pd the property descriptor
   * @return the property's write method
   */
  private Method getWriteMethod(PropertyDescriptor pd, Class<?> type, Class<?> propertyType) {

    final String capitalName = capitalize(pd.getName());
    final Class<?> parameterType = propertyType != null ? propertyType : pd.getPropertyType();
    Method writeMethod = pd.getWriteMethod();

    if (writeMethod == null && !("Class".equals(capitalName) && Class.class.equals(
        parameterType))) {
      /*
       * Special handling for older versions of Introspector: if
       * one of the getter or setter is fulfilling a templated type
       * and the other is not, they may not be returned as the same
       * property descriptor
       */
      try {
        writeMethod = type.getMethod("set" + capitalName, parameterType);
      } catch (NoSuchMethodException e) {
        writeMethod = null;
      }
    }

    if (writeMethod == null) {
      /*
       * Special handling for fluid APIs where setters return
       * a value
       */
      try {
        writeMethod = type.getMethod("set" + capitalName, parameterType);
      } catch (NoSuchMethodException e) {
        writeMethod = null;
      }
    }
    return writeMethod;
  }

  /**
   * The annotation @java.beans.Transient is available since Java 7. To ensure backward
   * compatibility we avoid using a class reference.
   *
   * @param readMethod The getter method
   * @return True, if annotated with @java.beans.Transient
   */
  private boolean isTransient(Method readMethod) {
    if (readMethod != null) {
      Annotation[] annotations = readMethod.getAnnotations();
      for (Annotation annotation : annotations) {
        if (annotation.annotationType().getName().equals("java.beans.Transient")) {
          return true;
        }
      }
    }
    return false;
  }


}
