package com.example.application.views;

import com.example.application.views.list.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

public class MainLayout extends AppLayout {

	public MainLayout() {
		createHeader();
		createDrawer();
	}

	private void createHeader() {
		H1 logo = new H1("송현권 CRM");
		logo.addClassNames("text-l", "m-m");

		HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		header.setWidth("100%");
		header.addClassNames("py-0", "px-m");
		Button btn = new Button("예제용 버튼");
		
		addToNavbar(header, btn);

	}

	private void createDrawer() {
		RouterLink listLink = new RouterLink("전체 목록", ListView.class);
		listLink.setHighlightCondition(HighlightConditions.sameLocation());

		addToDrawer(new VerticalLayout(listLink, new RouterLink("대시 보드", DashboardView.class)));
	}
}