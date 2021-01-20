package havis.net.ui.middleware.client.configuration;

import havis.net.ui.shared.client.ConfigurationSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProductInfoSection extends ConfigurationSection {

	private String documentation;
	
	@UiField
	Anchor homepage;
	@UiField
	Label name;
	@UiField
	Label version;	
	@UiField
	Label author;	
	
	private static ProductInfoSectionUiBinder uiBinder = GWT.create(ProductInfoSectionUiBinder.class);

	interface ProductInfoSectionUiBinder extends UiBinder<Widget, ProductInfoSection> {
	}

	@UiConstructor
	public ProductInfoSection(String name) {
		super(name);
		initWidget(uiBinder.createAndBindUi(this));
		homepage.setTarget("_blank");
	}
		
	public void setName(String name) {
		this.name.setText(name);
	}

	public void setVersion(String version) {
		this.version.setText(version);
	}

	public void setAuthor(String author) {
		this.author.setText(author);
	}

	public void setDocumentation(String url) {
		documentation = url;
	}

	public void setHomepage(String url) {
		homepage.setHref(url);
		homepage.setText(url);
	}
	
	@UiHandler("download")
	public void onDownload(ClickEvent event) {
		Window.open(documentation, "_blank", "");
	}
}
