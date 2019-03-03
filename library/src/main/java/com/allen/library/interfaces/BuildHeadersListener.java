package com.allen.library.interfaces;

import java.util.Map;

/**
 * <pre>
 *      @author : Allen
 *      e-mail  : lygttpod@163.com
 *      date    : 2019/03/03
 *      desc    : 请求头interface
 * </pre>
 */
public interface BuildHeadersListener {
    Map<String, String> buildHeaders();
}
