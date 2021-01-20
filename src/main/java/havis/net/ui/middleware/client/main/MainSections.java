package havis.net.ui.middleware.client.main;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import havis.net.ui.middleware.client.shared.IsMainSection;
import havis.net.ui.middleware.client.shared.SectionType;

public class MainSections extends FlowPanel implements AcceptsOneWidget, MainSectionsView {

	public HashMap<SectionType, MainSection> sections = new HashMap<>();
	private Presenter presenter;
	private SectionType currentSectionType;

	public MainSections() {
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	public void setWidget(final IsWidget w) {
		if (w != null && w instanceof IsMainSection) {
			final MainSection ms = sections.get(((IsMainSection) w).getSectionType());
			if (ms != null) {
				ms.add(w);
				ms.getElement().getStyle().setHeight(Window.getClientHeight() - ms.getHeaderHeight() - 30, Unit.PX);
				ms.setOpen(true);

				new Timer() {

					@Override
					public void run() {
						ms.getElement().getStyle().clearHeight();
					}
				}.schedule(500); // must correspond with the value from the css transition
				for (MainSection s : sections.values()) {
					if (s != ms) {
						s.setHidden(true);
					}
				}
				currentSectionType = ms.getSectionType();
			}
		}
	}

	@Override
	public void add(final Widget w) {
		if (w instanceof MainSection) {
			final MainSection ms = (MainSection) w;
			ms.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (currentSectionType != ms.getSectionType()) {
						presenter.onOpenSection(ms.getSectionType());
					} else {
						presenter.onCloseSection(ms);
					}
				}
			});
			sections.put(((MainSection) w).getSectionType(), (MainSection) w);
		}
		super.add(w);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.setView(this);
	}

	@Override
	public Map<SectionType, MainSection> getSections() {
		return sections;
	}
	
	@Override
	public void resetCurrentSection() {
		currentSectionType = null;
	}
	
	public void closeSubSection() {
		presenter.onCloseSection(sections.get(currentSectionType));
	}
}
