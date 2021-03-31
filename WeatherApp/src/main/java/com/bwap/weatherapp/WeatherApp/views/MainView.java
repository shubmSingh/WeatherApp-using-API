package com.bwap.weatherapp.WeatherApp.views;

import com.bwap.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import elemental.json.JsonException;
import elemental.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@SpringUI(path ="")
public class MainView extends UI {
    @Autowired
    private WeatherService weatherService;

    private VerticalLayout mainLayout;
    private NativeSelect<String> UnitSelect;
    private TextField cityTextField;
    private Button searchButton;
    private HorizontalLayout Dashboard;
    private Label Location;
    private Label currentTemp;
    private HorizontalLayout mainDesscriptionLayout;
    private Label weatherDescription;
    private Label MaxWeather;
    private Label MinWeather;
    private Label Humidity;
    private Label pressure;
    private Label Wind;
    private Label FeelsLike;
    private Image iconImg;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout();
        setHeader();
        setLogo();
        setForm();
        DashboardTitle();
        dashboardDetails();

        searchButton.addClickListener(clickEvent -> {
            if (!cityTextField.getValue().equals("")) {
                try {
                    updateUI();
                } catch (JsonException | JSONException e) {
                    e.printStackTrace();
                }
            } else
                Notification.show("Please Enter the city:");

        });
    }
        private void mainLayout () {
            iconImg = new Image();
            mainLayout = new VerticalLayout();
            mainLayout.setWidth("100%");
            mainLayout.setSpacing(true);
            mainLayout.setMargin(true);
            mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            setContent(mainLayout);
        }
        private void setHeader () {
            HorizontalLayout header = new HorizontalLayout();
            header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            Label title = new Label("Weather App By Shubham ");
            header.addComponent(title);
            mainLayout.addComponents(header);
        }
        private void setLogo () {
            HorizontalLayout logo = new HorizontalLayout();
            logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            Image img = new Image(null, new ClassResource("/static/logo.png"));
            logo.setWidth("240px");
            logo.setHeight("240px");
            logo.addComponent(img);
            mainLayout.addComponents(logo);

        }
        private void setForm () {
            HorizontalLayout formLayout = new HorizontalLayout();
            formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            formLayout.setSpacing(true);
            formLayout.setMargin(true);

            UnitSelect = new NativeSelect<>();
            ArrayList<String> items = new ArrayList<>();
            items.add("C");
            items.add("F");

            UnitSelect.setItems(items);
            UnitSelect.setValue(items.get(0));
            formLayout.addComponent(UnitSelect);

            //CityTextField
            cityTextField = new TextField();
            cityTextField.setWidth("80%");
            formLayout.addComponent(cityTextField);

            //Search Button
            searchButton = new Button();
            searchButton.setIcon(VaadinIcons.SEARCH);
            formLayout.addComponent(searchButton);

            mainLayout.addComponents(formLayout);

        }
        private void DashboardTitle () {
            Dashboard = new HorizontalLayout();
            Dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


            //city location
            Location = new Label("Currently in : ");
            Location.addStyleName(ValoTheme.LABEL_H2);
            Location.addStyleName(ValoTheme.LABEL_LIGHT);

            //current temprature
            currentTemp = new Label("");
            currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
            currentTemp.setStyleName(ValoTheme.LABEL_H1);

            Dashboard.addComponents(Location, iconImg, currentTemp);
            mainLayout.addComponents(Dashboard);

        }
        private void dashboardDetails () {
            mainDesscriptionLayout = new HorizontalLayout();
            mainDesscriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            //description layout
            VerticalLayout descriptionLayout = new VerticalLayout();
            descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            //Weather Description
            weatherDescription = new Label("Description: ");
            weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
            descriptionLayout.addComponents(weatherDescription);

            //Min Weather
            MinWeather = new Label("Min:");
            descriptionLayout.addComponents(MinWeather);

            //Max Weather
            MaxWeather = new Label("Max:");
            descriptionLayout.addComponents(MaxWeather);

            VerticalLayout pressureLayout = new VerticalLayout();
            pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

            pressure = new Label("Pressure: ");
            pressureLayout.addComponents(pressure);

            Humidity = new Label("Humidity: ");
            pressureLayout.addComponents(Humidity);

            Wind = new Label("Wind: ");
            pressureLayout.addComponents(Wind);

            FeelsLike = new Label("Feels Like: ");
            pressureLayout.addComponents(FeelsLike);

            mainDesscriptionLayout.addComponents(descriptionLayout, pressureLayout);
            mainLayout.addComponents(mainDesscriptionLayout);

        }

    /**
     *
     */
    private void updateUI () throws JSONException {
        String city = cityTextField.getValue();
        String defaultUnit;
        weatherService.setCityName(city);
      // this is used for show data in centigrade  and fehrenheit
        if(UnitSelect.getValue().equals("F")){
            weatherService.setUnit("imperials");
        UnitSelect.setValue("F");
        defaultUnit = "\u00b0"+"F";
        }
        else{
            weatherService.setUnit("metric");
            defaultUnit= "\u00b0"+"C";
            UnitSelect.setValue("C");
        }

        Location.setValue("Currently in "+city);
        JSONObject mainobject = weatherService.returnMain();

        int temp = mainobject.getInt("temp");
        currentTemp.setValue(temp +defaultUnit);


        String iconCode= null;
        String weatherDescriptionNew=null;
        JSONArray jsonArray= weatherService.returnWeatherArray();
        // this loop is used to take icon from the array that is there in weather ,json file
        for(int i=0; i<jsonArray.length(); i++)
         {
             JSONObject weatherObj= jsonArray.getJSONObject(i);
             iconCode= weatherObj.getString("icon");
             weatherDescriptionNew =weatherObj.getString("description");

         }
         // it is icon image between city name and temparature
        iconImg.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+"@2x.png"));

        weatherDescription.setValue("Description:"+weatherDescriptionNew);
        MinWeather.setValue("Min Temp:"+weatherService.returnMain().getInt("temp_min")+UnitSelect.getValue());
        MaxWeather.setValue("Max Temp:"+weatherService.returnMain().getInt("temp_max")+UnitSelect.getValue());
        pressure.setValue("Pressure:"+weatherService.returnMain().getInt("pressure"));
        Humidity.setValue("Humidity:"+weatherService.returnMain().getInt("humidity"));
        Wind.setValue("Wind:"+ weatherService.returnWind().getInt("speed"));
        FeelsLike.setValue("Feels Like:"+weatherService.returnMain().getDouble("feels_like"));



        }
}
