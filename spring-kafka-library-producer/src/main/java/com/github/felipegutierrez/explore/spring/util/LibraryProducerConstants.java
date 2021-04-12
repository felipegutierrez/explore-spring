package com.github.felipegutierrez.explore.spring.util;

public class LibraryProducerConstants {
    public static final String LIBRARY_V1_ENDPOINT = "/v1/libraryevent";
    public static final String LIBRARY_V1_SYNC_ENDPOINT = "/v1/sync/libraryevent";

    public static final String LIBRARY_AVRO_V1_ENDPOINT = "/v1/libraryevent/avro";
    public static final String LIBRARY_AVRO_V1_SYNC_ENDPOINT = "/v1/sync/libraryevent/avro";

    public static final String LIBRARY_V1_TOPIC = "library-events";

    public static final String LIBRARY_REACT_V1_ENDPOINT = "/v1/react/libraryevent";
    public static final String LIBRARY_FUNC_V1_ENDPOINT = "/v1/func/libraryevent";

    public static final String LIBRARY_ERROR_ID_NULL = "LibraryEvent ID is null";
    public static final String LIBRARY_ERROR_CREATE_ID_NOT_NULL = "LibraryEvent ID must be null for this operation";
}
