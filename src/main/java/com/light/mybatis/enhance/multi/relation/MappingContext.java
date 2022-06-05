package com.light.mybatis.enhance.multi.relation;

import com.light.mybatis.enhance.multi.Properties;
import com.light.mybatis.enhance.multi.relation.cern.colt.map.OpenIntObjectHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 映射上下文
 */
public class MappingContext {
  protected boolean containsCycle = true;
  protected Map<Object, Object> globalProperties;

  protected final OpenIntObjectHashMap typeCache;
  protected boolean capturesFieldContext;

  public MappingContext(Map<Object, Object> globalProperties) {
    this.typeCache = new OpenIntObjectHashMap();
    this.globalProperties = globalProperties;
    Boolean capture = globalProperties != null ? (Boolean)globalProperties.get(Properties.CAPTURE_FIELD_CONTEXT) : null;
    this.capturesFieldContext = capture == null || capture;
  }

  public void reset() {
    typeCache.clear();
  }

  public static class Factory implements  MappingContextFactory{

    LinkedBlockingQueue<MappingContext> contextQueue = new LinkedBlockingQueue<>();
    ConcurrentHashMap<Object, Object> globalProperties = new ConcurrentHashMap<>();
    @Override
    public MappingContext getContext() {
      MappingContext context = contextQueue.poll();
      if (context == null) {
        context = new MappingContext(globalProperties);
      }
      context.containsCycle = true;
      return null;
    }

    @Override
    public void release(MappingContext context) {
      context.reset();
      contextQueue.offer(context);
    }

    @Override
    public Map<Object, Object> getGlobalProperties() {
      return globalProperties;
    }
  }
}
