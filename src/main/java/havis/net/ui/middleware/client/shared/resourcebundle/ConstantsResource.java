package havis.net.ui.middleware.client.shared.resourcebundle;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Messages;

public interface ConstantsResource extends Messages {

	public static final ConstantsResource INSTANCE = GWT.create(ConstantsResource.class);
	
	String afterError();
	String aleId();
	String antenna();
	String antennaShort();
	String assetType();
	String association();
	String author();
	
	String bank();
	String boundaries();
	
	String cache();
	String cancelButtonLabel();
	String capsCount();
	String capsEpc();
	String capsRawDec();
	String capsRawHex();
	String capsTag();
	String chooseItem(String name);
	String clientid();
	String cmdOperation();
	String commandCycle();
	String commands();
	String common();
	String companyPrefix();
	String composite();
	String condition();
	String conditions();
	String connectionString();
	String columnMappingList();
	String count();
	String createItem(String name);
	
	String data();
	String dataSource();
	String dataType();
	String date();
	String defaultGroupName();
	String delete();
	String deleteReally(String name);
	String deleteRow();
	String documentation();
	String documentType();
	String download();
	String duration();
	
	String ecIncludeInReport();
	String ecTitleEventCycle();
	String entries();
	String epc();
	String errorAlreadyDefined(String field, String name);
	String errorAlreadyDefinedHere(String field);
	String errorAlreadyExists(String object, String name);
	String errorInvalid(String name);
	String errorInvalidChars(String name);
	String errorInvalidEmptyField(String name);
	String errorInvalidFieldNameSpecialChar();
	String errorInvalidFieldNameWhiteSpaces();
	String errorInvalidFieldWhiteSpaces(String name);
	String errorInvalidLength();
	String errorInvalidOffset();
	String errorInvalidOrGreater(String name, String measure);
	String errorInvalidSpecNameSpecialChar();
	String errorInvalidSpecNameWhiteSpaces();
	String errorIsInvalid(String value);
	String errorMustBeGreater(String name, String value);
	String errorNoIncludeExclude();
	String errorNegTimeValue(String name);
	String errorInValidTimeValue(String name);
	String errorNoHexValue(String name);
	String errorNoPatterns();
	String errorNoReaders();
	String errorNotANumber(String name);
	String errorNotSpecified(String name);
	String errorNoXSpecified(String name);
	String errorNotUCHAR();
	String errorOffsetGreaterPeriod();
	String errorOutOfRange();
	String errorSelectListItem();
	String errorSetNotSelected();
	String errorUnableToImportSpec();
	String errorUnicodeFound(String value);
	String eventCount();
	String events();
	String eventStatistics();
	String eventStatisticsRep();
	String eventTimestamps();
	String exclude();
	String export();
	String extension();
	
	String field();
	String fieldName();
	String fields();
	String fieldtype();
	String filter();
	String filterPattern();
	String first();
	String format();
	String fragment();
	
	String group();
	String groupPattern();
	String groupPatternSyntax();
	
	String homepage();
	String host();
	String hours();
	
	String id();
	String importStr();
	String include();
	String includeFieldSpecInReport();
	String individualAssetRef();
	String initiation();
	String initiationCondition();
	String initiationTrigger();
	String itemReference();
	
	String last();
	String length();
	String lifetime();
	String locationReference();
	String logicalReader();
	
	String managerNumber();
	String maxThreads();
	String milliseconds();
	String minutes();
	String moveDown();
	String moveUp();
	
	String name();
	String newEntry();
	String no();
	String noNewGpioEvents();
	String noNewTagsInterval();
	
	String objectClass();
	String offset();
	String oid();
	String okButtonLabel();
	String operation();
	String operations();
	String operationReport();
	String output();
	
	String path();
	String pattern();
	String patternList();
	String patternSyntax();
	String payload();
	String period();
	String pcTitlePortCycle();
	String pin();
	String pinType();
	String plain();
	String port();
	String portCycleReport();
	String portOperation();
	String primaryKeys();
	String productInformation();
	String profile();
	
	String query();
	String qos();
	String qualityOfService();
	String qosAtMostOnce();
	String qosAtLeastOnce();
	String qosExactlyOnce();
	
	String random();
	String reader();
	String readerCycle();
	String readerName();
	String readers();
	String readerSightings();
	String readerStatistics();
	String repeatPeriod();
	String report();
	String reports();
	String repReportIfEmpty();
	String repReportOnChangeOnly();
	String run();
	
	String scheme();
	String seconds();
	String serialNumber();
	String serialReference();
	String sightingSignal();
	String sightingSignals();
	String source();
	String stableSet();
	String standardVersion();
	String startTrigger();
	String state();
	String statistics();
	String status();
	String stopTrigger();
	String strength();
	String strengthShort();
	String subscriberEditorTitle();
	String subscriberUserInfo();
	String subscriberActive();
	String subscriberInactive();
	String subscriberSection();
	
	String tablename();
	String tagCount();
	String tagMemory();
	String tagsProcessedCnt();
	String tagStatisticsRep();
	String tagTimestamp();
	String termination();
	String terminationCondition();
	String terminationTrigger();
	String threads();
	String timeout();
	String timestamp();
	String topic();
	String totalCount();
	String totalMilliseconds();
	String trigger();
	String type();
	
	String uri();
	String unknownParameter(String name);
	
	String value();
	
	String whenDataAvailable();
	
	String yes();
	
	String force();
	
	String oidListAllDataFormats();
	String oidListDataFormat9();
	String oidListDataFormat3();
}