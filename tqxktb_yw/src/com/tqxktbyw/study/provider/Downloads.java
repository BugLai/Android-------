package com.tqxktbyw.study.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.tqxktbyw.study.EastStudy;

public final class Downloads {
	/**
	 * The authority for the EastStudy provider
	 */
	public static final String AUTHORITY = "com.tqxktbyw.study.EastStudy";

    /**
     * The content:// style URL for this table
     */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/downloads");

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of downloads.
     */
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.tqxktbyw.downloads";

    /**
     * The MIME type of a {@link #CONTENT_URI} sub-directory of a single download.
     */
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.tqxktbyw.downloads";
	
	public interface ExpandableList {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(Downloads.CONTENT_URI, "grade");
        public static final Uri CONTENT_ITEM_URI = Uri.withAppendedPath(Downloads.CONTENT_URI, "expandablelist");
    
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of success.
         */
        public static final String CONTENT_TYPE = Downloads.CONTENT_TYPE + ".expandablelist";
    
        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single success.
         */
        public static final String CONTENT_ITEM_TYPE = Downloads.CONTENT_ITEM_TYPE + ".expandablelist";
    }

    public interface Success {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(Downloads.CONTENT_URI, "success");
    
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of success.
         */
        public static final String CONTENT_TYPE = Downloads.CONTENT_TYPE + ".success";
    
        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single success.
         */
        public static final String CONTENT_ITEM_TYPE = Downloads.CONTENT_ITEM_TYPE + ".success";
    }

    public interface Execute {
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(Downloads.CONTENT_URI, "execute");
    
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of execute.
         */
        public static final String CONTENT_TYPE = Downloads.CONTENT_TYPE + ".execute";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single execute.
         */
        public static final String CONTENT_ITEM_TYPE = Downloads.CONTENT_ITEM_TYPE + ".execute";
    }

    /**
     * The unique ID for a row.
     * <P>Type: INTEGER (long)</P>
     */
    public static final String _ID = "_id";
    
    public static final String LID = "lid";

    /**
     * The download url of the table
     * <P>Type: TEXT</P>
     */
    public static final String DOWNLOAD_URL = "download_url";

    /**
     * The lession name of the table
     * <P>Type: TEXT</P>
     */
    public static final String LESSION_NAME = "lession_name";

    /**
     * The lession type of the table
     * <P>Type: INTEGER</P>
     */
    public static final String LESSION_TYPE = "lession_type";

    /**
     * The status of the table
     * <P>Type: INTEGER</P>
     */
    public static final String STATUS = "status";

    /**
     * The filename of the table
     * <P>Type: TEXT</P>
     */
    public static final String FILENAME = "filename";

    /**
     * The total size of the table
     * <P>Type: INTEGER(long)</P>
     */
    public static final String TOTAL_SIZE = "total_size";

    /**
     * The downloaded size of the table
     * <P>Type: INTEGER(long)</P>
     */
    public static final String DOWNLOADED_SIZE = "downloaded_size";

    /**
     * The sort order of the table
     * <P>Type: INTEGER</P>
     */
    public static final String SORT_ORDER = "sort_order";

    /**
     * The course id of the table
     * <P>Type: INTEGER(long)</P>
     */
    public static final String COURSE_ID = "course_id";
    
    public static final String ISV = "isv";

    public static final String ZIPNAME = "zipname";

    /**
     * The default sort order for table
     */
    public static final String DEFAULT_SORT_ORDER = SORT_ORDER + " ASC";

    public static final String PLAY_COLUMNS[] = {
        Downloads.FILENAME
    };
    public static final String DOWNLOADING_COLUMNS[] = {
    	LID,ISV,DOWNLOAD_URL
    };
    public static final int DOWNLOADS_TYPE_VIDEO           = 0;
    public static final int DOWNLOADS_TYPE_ZIP     = 1;
    public static final int DOWNLOADS_TYPE_MP4   = 2;
    public static final int DOWNLOADS_TYPE[] = {
    	DOWNLOADS_TYPE_VIDEO,DOWNLOADS_TYPE_ZIP,DOWNLOADS_TYPE_MP4
    };
    public static final int DOWNLOADS_TYPE_VIDEO_INDEX           = 0;
    public static final int DOWNLOADS_TYPE_ZIP_INDEX     = 1;
    public static final int DOWNLOADS_TYPE_MP4_INDEX   = 2;

    public static final int PLAY_COLUMN_FILENAME_INDEX = 0;

    public static final String DOWNLOAD_COLUMNS[] = {
        Downloads._ID,
        Downloads.FILENAME,
        Downloads.TOTAL_SIZE,
        Downloads.LESSION_NAME,
        Downloads.LESSION_TYPE
    };

    public static final int DOWNLOAD_COLUMN_ID_INDEX           = 0;
    public static final int DOWNLOAD_COLUMN_FILENAME_INDEX     = 1;
    public static final int DOWNLOAD_COLUMN_TOTAL_SIZE_INDEX   = 2;
    public static final int DOWNLOAD_COLUMN_LESSION_NAME_INDEX = 3;
    public static final int DOWNLOAD_COLUMN_LESSION_TYPE_INDEX = 4;

    public static final String DOWNLOAD_MANAGER_COLUMNS[] = {
        Downloads._ID,
        Downloads.STATUS,
        Downloads.FILENAME,
        Downloads.TOTAL_SIZE,
        Downloads.LESSION_NAME,
        Downloads.LESSION_TYPE,
        Downloads.DOWNLOAD_URL,
        Downloads.DOWNLOADED_SIZE,
    };

    public static final int DOWNLOAD_MANAGER_COLUMN_ID_INDEX           = 0;
    public static final int DOWNLOAD_MANAGER_COLUMN_STATUS_INDEX       = 1;
    public static final int DOWNLOAD_MANAGER_COLUMN_FILENAME_INDEX     = 2;
    public static final int DOWNLOAD_MANAGER_COLUMN_TOTAL_SIZE_INDEX   = 3;
    public static final int DOWNLOAD_MANAGER_COLUMN_LESSION_NAME_INDEX = 4;
    public static final int DOWNLOAD_MANAGER_COLUMN_LESSION_TYPE_INDEX = 5;
    public static final int DOWNLOAD_MANAGER_COLUMN_DOWNLOAD_URL_INDEX = 6;
    public static final int DOWNLOAD_MANAGER_COLUMN_DOWNLOADED_SIZE_INDEX = 7;

	public static final void moveExecuteToSuccess(ContentResolver resolver, long id, long downloadedSize,int isv) {
		move(resolver, id, DownloadProvider.EXECUTE_TABLE, DownloadProvider.SUCCESS_TABLE, EastStudy.STATUS_SUCCESSFUL, downloadedSize,isv);
	}

	public static final void moveSuccessToExecute(ContentResolver resolver, long id,int isv) {
		move(resolver, id, DownloadProvider.SUCCESS_TABLE, DownloadProvider.EXECUTE_TABLE, EastStudy.STATUS_PENDING, 0,isv);
	}

	private static final void move(ContentResolver resolver, long id, String sourceTable, String destTable, int status, long downloadedSize,int isv) {
		ContentValues values = new ContentValues();
		values.put(_ID, id);
		values.put(SOURCE_TABLE, sourceTable);
		values.put(DEST_TABLE, destTable);
		values.put(STATUS, status);
		values.put(DOWNLOADED_SIZE, downloadedSize);
		values.put(ISV, isv);

		resolver.update(CONTENT_MOVE_URI, values, null, null);
	}
	
	static final Uri CONTENT_MOVE_URI = Uri.withAppendedPath(Downloads.CONTENT_URI, "move");
	static final String DEST_TABLE = "dest";
	static final String SOURCE_TABLE = "source";

	private Downloads() {
	}
}