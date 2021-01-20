package havis.net.ui.middleware.client.main;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import havis.net.ui.middleware.client.place.CommonPlace;
import havis.net.ui.middleware.client.place.ListPlace;
import havis.net.ui.middleware.client.place.ListType;
import havis.net.ui.middleware.client.shared.SectionType;
import havis.net.ui.middleware.client.shared.event.ServiceEvent;

public class MainSectionsPresenter implements MainSectionsView.Presenter {

	@Inject
	private PlaceController placeController;

	private MainSectionsView view;

	private Place parentPlace;
	
	private Timer aniTimer;

	@Inject
	public MainSectionsPresenter(EventBus eventBus) {
		ServiceEvent.register(eventBus, new ServiceEvent.Handler() {

			@Override
			public void onServiceEvent(ServiceEvent event) {
				onCloseSection(view.getSections().get(event.getSectionType()));
			}
		});
	}

	@Override
	public void onOpenSection(SectionType sectionType) {
		if (aniTimer != null) {
			aniTimer.cancel();
		}
		if (sectionType == SectionType.CM) {
			placeController.goTo(new CommonPlace("0"));
		} else {
			placeController.goTo(new ListPlace(ListType.valueOf(sectionType.name())));
		}
	}

	@Override
	public void onCloseSection(final MainSection ms) {
		if (ms != null) {
			final Widget w = ms.getWidget(1);
			if (w != null && w instanceof MainSections) {
				((MainSections) w).closeSubSection();
			}
			ms.getElement().getStyle().setHeight(Window.getClientHeight() - ms.getHeaderHeight() - 30, Unit.PX);
			placeController.goTo(parentPlace);
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

				@Override
				public void execute() {
					ms.getElement().getStyle().clearHeight();
				}
			});
			aniTimer = new Timer() {

				@Override
				public void run() {
					if (w != null) {
						if (!(w instanceof MainSections)) {
							ms.remove(1);
						}
					}
				}
			};
			aniTimer.schedule(500); // must correspond with the value from
								// the css
			for (MainSection s : view.getSections().values()) {
				s.setHidden(false);
				s.setOpen(false);
			}
			view.resetCurrentSection();
		}
	}

	/**
	 * @param parentPlace
	 *            the parentPlace to set
	 */
	public void setParentPlace(Place parentPlace) {
		this.parentPlace = parentPlace;
	}

	@Override
	public void setView(MainSectionsView view) {
		this.view = view;
	}

}
