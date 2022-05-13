package com.example.application.views;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.application.data.service.CrmService;
import com.example.application.vo.TrafficCount;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.DataSeriesItemTimeline;
import com.vaadin.flow.component.charts.model.MarkerSymbolEnum;
import com.vaadin.flow.component.charts.model.PlotOptionsTimeline;
import com.vaadin.flow.component.charts.model.Series;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
public class DashboardView extends VerticalLayout {
	private final CrmService service;

	public DashboardView(CrmService service) {
		this.service = service;
		addClassName("dashboard-view");
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		add(getContactStats(), getCompaniesChart() , getTOTestChart());
	}

	private VerticalLayout getTOTestChart() {
	  VerticalLayout serviceUserLayout = new VerticalLayout();
	  
	  List<TrafficCount> userTrafficList = new ArrayList<TrafficCount>();
	  
	  for(int i = 0 ; i < 10 ; i++ ) {
	    TrafficCount obj = new TrafficCount();
	    obj.setId(i);
	    obj.setName(i+"name");
	    obj.setHost(i+"Host");
	    obj.setDate(LocalDateTime.now().plusHours(i));
	    userTrafficList.add(obj);
	  }
    Grid<TrafficCount> trafficCountGrid = new Grid<TrafficCount>();
    trafficCountGrid.addColumn(i -> {
      return DateTimeFormatter.ofPattern("HH:mm:ss").format(i.getDate());
    }).setHeader("접속시간");
    trafficCountGrid.addColumn(i -> i.getId()).setHeader("id").setVisible(false);
    trafficCountGrid.addColumn(i -> i.getName()).setHeader("시스템 명");
    trafficCountGrid.addColumn(i -> i.getHost()).setHeader("도메인");
    trafficCountGrid.setItems(userTrafficList);
    trafficCountGrid.setWidthFull();
    trafficCountGrid.setHeight("300px");
    Chart chart = new Chart(ChartType.TIMELINE);
    Configuration conf = chart.getConfiguration();
    conf.getTooltip().setEnabled(true); // 툴팁 사용

    // Add data
    //List<DataSeriesItem> seriesDate = new ArrayList<>();
    DataSeries series = new DataSeries();
    HashMap<Integer, TrafficCount> seriesMap = new HashMap<Integer, TrafficCount>();
     for (TrafficCount vo : userTrafficList) {
      DataSeriesItem temp = new DataSeriesItemTimeline(getInstant(vo.getDate()),
          DateTimeFormatter.ofPattern("HH:mm:ss").format(vo.getDate()) , 
          String.valueOf(vo.getId()) , // 라벨 미표시로 인덱스 저장
          vo.getName() + "<br/>" + vo.getHost());
      seriesMap.put(series.getData().size(), vo); //0부터 시작 
      series.add(temp); //추가되면 1
      
    }
    PlotOptionsTimeline options = new PlotOptionsTimeline();
    options.getMarker().setSymbol(MarkerSymbolEnum.CIRCLE);
    options.getDataLabels().setEnabled(false);
    series.setPlotOptions(options);
    conf.setSeries(series);
        // Configure the axes
    conf.getxAxis().setVisible(false);
    conf.getxAxis().setType(AxisType.DATETIME);
    conf.getyAxis().setVisible(false);
    chart.setWidthFull();
    chart.setHeight("30%");
    chart.addPointClickListener(event -> {
      int row = event.getItemIndex();
      trafficCountGrid.select(seriesMap.get(row));
      trafficCountGrid.scrollToIndex(row);
    });
    
    serviceUserLayout.add(chart , trafficCountGrid);
    return serviceUserLayout;
  }

  private Component getContactStats() {
		Span stats = new Span(service.countContacts() + " contacts");
		stats.addClassNames("text-xl", "mt-m");
		return stats;
	}

	private Chart getCompaniesChart() {
		Chart chart = new Chart(ChartType.PIE);

		DataSeries dataSeries = new DataSeries();
		service.findAllCompanies()
				.forEach(company -> dataSeries.add(new DataSeriesItem(company.getName(), company.getEmployeeCount())));
		chart.getConfiguration().setSeries(dataSeries);
		return chart;
	}
	
  private Instant getInstant(LocalDateTime date) {
    Instant instant = date.atZone(ZoneId.of("Europe/Paris")).toInstant();
    return instant;
  }

  private Instant getInstant(int year, int month, int dayOfMonth) {
    return LocalDate.of(year, month, dayOfMonth).atStartOfDay().toInstant(ZoneOffset.UTC);
  }
}