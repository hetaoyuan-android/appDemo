/**
 * com.genew.base.permission
 * PermissionListener.java
 * 2020-12-07 liangjiahao
 * Copyright (c) Genew Technologies 2010-2016. All rights reserved.
 */
package com.example.test.utils;

import androidx.annotation.NonNull;

/**
 * TODO Add class comment here<p/>
 * @version 1.0.0
 * @since 1.0.0
 * @author liangjiahao
 * @history<br/>
 * ver    date       author desc
 * 1.0.0  2020-12-07 liangjiahao created<br/>
 * <p/>
 */
public interface PermissionListener
{
	/**
	 * 通过授权
	 * @param permission
	 */
	void permissionGranted(@NonNull String[] permission);

	/**
	 * 拒绝授权
	 * @param permission
	 */
	void permissionDenied(@NonNull String[] permission);
}
