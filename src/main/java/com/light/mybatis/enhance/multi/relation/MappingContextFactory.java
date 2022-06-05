package com.light.mybatis.enhance.multi.relation;

import java.util.Map;

public interface MappingContextFactory {

  /**
   * Gets an available instance of MappingContext
   *
   * @return an instance of MappingContext
   */
  MappingContext getContext();

  /**
   * Allows for implementations that reuse objects to clean-up/clear any resources
   * associated with the particular context instance once it is no longer needed.
   *
   * @param context the context to be recycled
   */
  void release(MappingContext context);


  /**
   * @return a reference to the global properties map for this mapping context factory; any properties
   * set here are available from any individual MappingContext created by this factory.
   */
  Map<Object, Object> getGlobalProperties();
}
