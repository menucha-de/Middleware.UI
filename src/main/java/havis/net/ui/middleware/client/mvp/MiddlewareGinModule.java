package havis.net.ui.middleware.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import havis.net.rest.middleware.cc.CCAsync;
import havis.net.rest.middleware.configuration.ConfigurationAsync;
import havis.net.rest.middleware.ec.ECAsync;
import havis.net.rest.middleware.lr.LRAsync;
import havis.net.rest.middleware.pc.PCAsync;
import havis.net.rest.middleware.tm.TMAsync;
import havis.net.rest.middleware.utils.UtilsAsync;
import havis.net.ui.middleware.client.AppLayout;
import havis.net.ui.middleware.client.cc.CommandCycleEditorActivity;
import havis.net.ui.middleware.client.cc.CommandCycleListActivity;
import havis.net.ui.middleware.client.cc.cmd.CCCmdSpecEditorActivity;
import havis.net.ui.middleware.client.cc.cmd.operation.CCOpSpecEditorActivity;
import havis.net.ui.middleware.client.cc.report.CCReportsEditorActivity;
import havis.net.ui.middleware.client.configuration.Common;
import havis.net.ui.middleware.client.configuration.CommonActivity;
import havis.net.ui.middleware.client.configuration.CommonView;
import havis.net.ui.middleware.client.ds.DataSourceSelection;
import havis.net.ui.middleware.client.ds.DataSourceSelectionActivity;
import havis.net.ui.middleware.client.ds.DataSourceSelectionView;
import havis.net.ui.middleware.client.ds.association.AssociationEntriesEditorActivity;
import havis.net.ui.middleware.client.ds.association.AssociationEditorActivity;
import havis.net.ui.middleware.client.ds.association.AssociationListActivity;
import havis.net.ui.middleware.client.ds.cache.CacheEditorActivity;
import havis.net.ui.middleware.client.ds.cache.CacheListActivity;
import havis.net.ui.middleware.client.ds.random.RandomEditorActivity;
import havis.net.ui.middleware.client.ds.random.RandomListActivity;
import havis.net.ui.middleware.client.ec.EventCycleEditorActivity;
import havis.net.ui.middleware.client.ec.EventCycleListActivity;
import havis.net.ui.middleware.client.ec.rep.ECReportSpecEditorActivity;
import havis.net.ui.middleware.client.ec.rep.group.ECGroupSpecPatternActivity;
import havis.net.ui.middleware.client.ec.rep.output.OutputFieldSpecEditorActivity;
import havis.net.ui.middleware.client.ec.report.ECReportsEditorActivity;
import havis.net.ui.middleware.client.lr.ReaderEditorActivity;
import havis.net.ui.middleware.client.lr.ReaderListActivity;
import havis.net.ui.middleware.client.main.MainSectionsPresenter;
import havis.net.ui.middleware.client.main.MainSectionsView;
import havis.net.ui.middleware.client.pc.PortCycleEditorActivity;
import havis.net.ui.middleware.client.pc.PortCycleListActivity;
import havis.net.ui.middleware.client.pc.rep.PCReportSpecEditorActivity;
import havis.net.ui.middleware.client.pc.rep.operation.PCOpSpecEditorActivity;
import havis.net.ui.middleware.client.pc.report.PCReportsEditorActivity;
import havis.net.ui.middleware.client.place.MainPlace;
import havis.net.ui.middleware.client.shared.EditorDialog;
import havis.net.ui.middleware.client.shared.EditorDialogView;
import havis.net.ui.middleware.client.shared.pattern.CachePatternItemEditorActivity;
import havis.net.ui.middleware.client.shared.pattern.FilterPatternItemEditorActivity;
import havis.net.ui.middleware.client.shared.report.filter.ECFilterListMemberEditorActivity;
import havis.net.ui.middleware.client.shared.spec.SpecItem;
import havis.net.ui.middleware.client.shared.spec.SpecItemList;
import havis.net.ui.middleware.client.shared.spec.SpecItemListView;
import havis.net.ui.middleware.client.shared.spec.SpecItemPresenter;
import havis.net.ui.middleware.client.shared.spec.SpecItemView;
import havis.net.ui.middleware.client.shared.storage.CCSpecStorage;
import havis.net.ui.middleware.client.shared.storage.CommonStorage;
import havis.net.ui.middleware.client.shared.storage.DASpecStorage;
import havis.net.ui.middleware.client.shared.storage.DCSpecStorage;
import havis.net.ui.middleware.client.shared.storage.DRSpecStorage;
import havis.net.ui.middleware.client.shared.storage.ECSpecStorage;
import havis.net.ui.middleware.client.shared.storage.LRSpecStorage;
import havis.net.ui.middleware.client.shared.storage.PCSpecStorage;
import havis.net.ui.middleware.client.shared.storage.TMSpecStorage;
import havis.net.ui.middleware.client.shared.trigger.TriggerEditorActivity;
import havis.net.ui.middleware.client.subscriber.SubscriberEditorActivity;
import havis.net.ui.middleware.client.tm.TagMemoryEditorActivity;
import havis.net.ui.middleware.client.tm.TagMemoryListActivity;

public class MiddlewareGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

		bind(AppLayout.class).in(Singleton.class);
		bind(CommonStorage.class).in(Singleton.class);
		bind(LRSpecStorage.class).in(Singleton.class);
		bind(TMSpecStorage.class).in(Singleton.class);
		bind(ECSpecStorage.class).in(Singleton.class);
		bind(PCSpecStorage.class).in(Singleton.class);
		bind(CCSpecStorage.class).in(Singleton.class);
		bind(DASpecStorage.class).in(Singleton.class);
		bind(DCSpecStorage.class).in(Singleton.class);
		bind(DRSpecStorage.class).in(Singleton.class);
		
		bind(ECAsync.class).in(Singleton.class);
		bind(PCAsync.class).in(Singleton.class);
		bind(CCAsync.class).in(Singleton.class);
		bind(LRAsync.class).in(Singleton.class);
		bind(TMAsync.class).in(Singleton.class);
		bind(UtilsAsync.class).in(Singleton.class);
		bind(ConfigurationAsync.class).in(Singleton.class);
		
		bind(AppPlaceHistoryMapper.class).in(Singleton.class);

		// Views
		bind(CommonView.class).to(Common.class).in(Singleton.class);
		bind(SpecItemListView.class).to(SpecItemList.class).in(Singleton.class);
		bind(EditorDialogView.class).to(EditorDialog.class).in(Singleton.class);
		bind(DataSourceSelectionView.class).to(DataSourceSelection.class).in(Singleton.class);
		
		bind(SpecItemView.Presenter.class).to(SpecItemPresenter.class);
		bind(SpecItemView.class).to(SpecItem.class);
		
		bind(MainSectionsView.Presenter.class).to(MainSectionsPresenter.class);
		
//		bind(DsCacheItemView.Presenter.class).to(DsCacheItemPresenter.class);
//		bind(DsCacheItemView.class).to(DsCacheItem.class);
//		bind(DsAssociationItemView.Presenter.class).to(DsAssociationItemPresenter.class);
//		bind(DsAssociationItemView.class).to(DsAssociationItem.class);
//		bind(DsRandomItemView.Presenter.class).to(DsRandomItemPresenter.class);
//		bind(DsRandomItemView.class).to(DsRandomItem.class);

		// ActivityMappers
		bind(ActivityMapper.class).annotatedWith(Names.named("content")).to(ContentActivityMapper.class).in(Singleton.class);
		bind(ActivityMapper.class).annotatedWith(Names.named("details")).to(DetailsActivityMapper.class).in(Singleton.class);
		bind(ActivityMapper.class).annotatedWith(Names.named("dataSource")).to(DataSourceActivityMapper.class).in(Singleton.class);

		// Activities
		bind(Activity.class).annotatedWith(Names.named("lr-list")).to(ReaderListActivity.class);
		bind(Activity.class).annotatedWith(Names.named("tm-list")).to(TagMemoryListActivity.class);
		bind(Activity.class).annotatedWith(Names.named("ec-list")).to(EventCycleListActivity.class);
		bind(Activity.class).annotatedWith(Names.named("cc-list")).to(CommandCycleListActivity.class);
		bind(Activity.class).annotatedWith(Names.named("pc-list")).to(PortCycleListActivity.class);
		bind(Activity.class).annotatedWith(Names.named("ds-select")).to(DataSourceSelectionActivity.class);
		bind(Activity.class).annotatedWith(Names.named("dsrn-list")).to(RandomListActivity.class);
		bind(Activity.class).annotatedWith(Names.named("dsas-list")).to(AssociationListActivity.class);
		bind(Activity.class).annotatedWith(Names.named("dsca-list")).to(CacheListActivity.class);
		bind(Activity.class).annotatedWith(Names.named("common")).to(CommonActivity.class);

		bind(Activity.class).annotatedWith(Names.named("lr-editor")).to(ReaderEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("tm-editor")).to(TagMemoryEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("ec-editor")).to(EventCycleEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("pc-editor")).to(PortCycleEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("cc-editor")).to(CommandCycleEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("cc-reps-editor")).to(CCReportsEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("pc-rep-editor")).to(PCReportSpecEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("pc-reps-editor")).to(PCReportsEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("pc-opspec-editor")).to(PCOpSpecEditorActivity.class);
		
		bind(Activity.class).annotatedWith(Names.named("sub-editor")).to(SubscriberEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("ec-reps-editor")).to(ECReportsEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("ec-rep-editor")).to(ECReportSpecEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("rep-outfield-editor")).to(OutputFieldSpecEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("rep-filter-editor")).to(ECFilterListMemberEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("rep-grouppattern-editor")).to(ECGroupSpecPatternActivity.class);
		bind(Activity.class).annotatedWith(Names.named("tr-editor")).to(TriggerEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("rep-filterpattern-editor")).to(FilterPatternItemEditorActivity.class);
		
		bind(Activity.class).annotatedWith(Names.named("cc-cmdspec-editor")).to(CCCmdSpecEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("cc-opspec-editor")).to(CCOpSpecEditorActivity.class);
		
		bind(Activity.class).annotatedWith(Names.named("dsca-editor")).to(CacheEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("dsas-editor")).to(AssociationEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("dsrn-editor")).to(RandomEditorActivity.class);
		
		bind(Activity.class).annotatedWith(Names.named("ds-cachepattern-editor")).to(CachePatternItemEditorActivity.class);
		bind(Activity.class).annotatedWith(Names.named("ds-associationentries-editor")).to(AssociationEntriesEditorActivity.class);
	}

	@Singleton
	@Provides
	public PlaceController getPlaceController(final EventBus eventBus) {
		return new PlaceController(eventBus);
	}

	@Singleton
	@Provides
	public PlaceHistoryHandler getPlaceHistoryHandler(final AppPlaceHistoryMapper historyMapper, final EventBus eventBus,
			final PlaceController placeController) {
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, new MainPlace("0"));
		return historyHandler;
	}

	@Singleton
	@Provides
	@Named("content")
	public ActivityManager getContentActivityManager(@Named("content") final ActivityMapper activityMapper, final EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Singleton
	@Provides
	@Named("details")
	public ActivityManager getDetailsActivityManager(@Named("details") final ActivityMapper activityMapper, final EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Singleton
	@Provides
	@Named("dataSource")
	public ActivityManager getDataSourceActivityManager(@Named("dataSource") final ActivityMapper activityMapper, final EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

}
