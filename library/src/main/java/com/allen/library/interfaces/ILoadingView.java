package com.allen.library.interfaces;

/**
 * <pre>
 *      @author : Allen
 *      e-mail  : lygttpod@163.com
 *      date    : 2019/03/02
 *      desc    : 接口化处理loadingView，突破之前只能用dialog的局限
 * </pre>
 */
public interface ILoadingView {
    /**
     * 显示loadingView
     */
    void showLoadingView();

    /**
     * 隐藏loadingView
     */
    void hideLoadingView();
}
