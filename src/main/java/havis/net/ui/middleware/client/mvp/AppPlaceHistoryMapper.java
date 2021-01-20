package havis.net.ui.middleware.client.mvp;

import havis.net.ui.middleware.client.place.CommonPlace;
import havis.net.ui.middleware.client.place.EditorPlace;
import havis.net.ui.middleware.client.place.ListPlace;
import havis.net.ui.middleware.client.place.MainPlace;
import havis.net.ui.middleware.client.place.SubscriberPlace;
import havis.net.ui.middleware.client.place.TriggerPlace;
import havis.net.ui.middleware.client.place.cc.CCCmdSpecEditorPlace;
import havis.net.ui.middleware.client.place.ds.AssociationEntriesEditorPlace;
import havis.net.ui.middleware.client.place.ds.CacheEditorPlace;
import havis.net.ui.middleware.client.place.ds.CachePatternEditorPlace;
import havis.net.ui.middleware.client.place.ec.ECReportSpecEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.FilterPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.GroupPatternItemEditorPlace;
import havis.net.ui.middleware.client.place.ec.OutputFieldItemEditorPlace;
import havis.net.ui.middleware.client.place.pc.OpSpecEditorPlace;
import havis.net.ui.middleware.client.place.pc.PcReportSpecItemEditorPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
	MainPlace.Tokenizer.class,
	CommonPlace.Tokenizer.class,
	ListPlace.Tokenizer.class,
	EditorPlace.Tokenizer.class,
	ECReportSpecEditorPlace.Tokenizer.class,
	OutputFieldItemEditorPlace.Tokenizer.class,
	GroupPatternItemEditorPlace.Tokenizer.class,
	FilterItemEditorPlace.Tokenizer.class,
	FilterPatternItemEditorPlace.Tokenizer.class,
	TriggerPlace.Tokenizer.class,
	SubscriberPlace.Tokenizer.class,
	PcReportSpecItemEditorPlace.Tokenizer.class,
	OpSpecEditorPlace.Tokenizer.class,
	CCCmdSpecEditorPlace.Tokenizer.class,
	CacheEditorPlace.Tokenizer.class,
	CachePatternEditorPlace.Tokenizer.class,
	AssociationEntriesEditorPlace.Tokenizer.class
})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
