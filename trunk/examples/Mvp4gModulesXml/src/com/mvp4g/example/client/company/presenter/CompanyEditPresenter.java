package com.mvp4g.example.client.company.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.example.client.company.bean.CompanyBean;
import com.mvp4g.example.client.company.view.CompanyEditView;

@Presenter( view = CompanyEditView.class )
public class CompanyEditPresenter extends AbstractCompanyPresenter {

	public void onGoToEdit( CompanyBean company ) {
		this.company = company;
		fillView();
		eventBus.dispatch( "changeBody", view.getViewWidget() );
	}

	public void onNameSelected( String name ) {
		view.getName().setValue( name );
		view.alert( "Name changed on edit page." );
	}

	@Override
	protected void clickOnLeftButton( ClickEvent event ) {
		fillBean();
		service.updateCompany( company, new AsyncCallback<Void>() {

			public void onSuccess( Void result ) {
				eventBus.dispatch( "goToDisplay", company );
				eventBus.dispatch( "displayMessage", "Update Succeeded" );
			}

			public void onFailure( Throwable caught ) {
				eventBus.dispatch( "displayMessage", "Update Failed" );
			}
		} );
	}

	@Override
	protected void clickOnRightButton( ClickEvent event ) {
		clear();
	}

}
