package com.admin.shareproject.shareUtils;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ShareContentType
 * Support Share Content Types.
 *
 * @author zdj
 * @date 2018/12/11 9:22
 */

@StringDef({ShareContentType.TEXT, ShareContentType.IMAGE,
        ShareContentType.AUDIO, ShareContentType.VIDEO, ShareContentType.FILE})
@Retention(RetentionPolicy.SOURCE)
public @interface ShareContentType {
    /**
     * Share Text
     */
    final String TEXT = "text/plain";

    /**
     * Share Image
     */
    final String IMAGE = "image/*";

    /**
     * Share Audio
     */
    final String AUDIO = "audio/*";

    /**
     * Share Video
     */
    final String VIDEO = "video/*";

    /**
     * Share File
     */
    final String FILE = "*/*";
}
