package com.light.mybatis.enhance.multi.relation.metadata;

import com.light.mybatis.enhance.multi.relation.property.PropertyResolver;
import lombok.Getter;

/**
 * 不可变的
 */
@Getter
public class Property {

  private final String getter;
  private final String setter;
  private final String expression;
  private final String name;
  private final Class<?> type;


  protected Property(final String expression, final String name, final String getter,
      final String setter, final Class<?> type) {
    super();
    this.expression = expression;
    this.name = name;
    this.getter = getter;
    this.setter = setter;
    this.type = type;
  }

  @Override
  public String toString() {
    return "Property{" +
        "getter='" + getter + '\'' +
        ", setter='" + setter + '\'' +
        ", expression='" + expression + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        '}';
  }

  public static class Builder {

    private String getter;
    private String setter;
    private String expression;
    private String name;
    private Class<?> type;
    private Property container;

    public Builder container(Property container) {
      this.container = container;
      return this;
    }

    public Builder expression(String expression) {
      this.expression = expression;
      return this;
    }

    public Builder type(Class<?> type) {
      this.type = type;
      return this;
    }


    public Builder name(String propertyName) {
      this.name = propertyName;
      return this;
    }

    public Builder getter(final String getter) {
      this.getter = getter;
      return this;
    }

    public Builder setter(final String setter) {
      this.setter = setter;
      return this;
    }

    public Property build() {
      return new Property(expression != null ? expression : name, name, getter, setter, type
      );
    }
  }


}
