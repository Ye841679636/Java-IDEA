package com.newpoint.ui;

import com.newpoint.instrument.InstrumentManager;
import com.newpoint.instrument.Security;
import com.newpoint.instrument.SecurityType;
import com.newpoint.marketdata.BarSize;
import com.newpoint.marketdata.MarketDataBlock;
import com.newpoint.marketdata.MarketDataProvider;
import com.newpoint.order.Transaction;
import com.newpoint.strategy.BollingerStrategy;
import com.newpoint.strategy.StrategyResult;
import com.newpoint.strategy.StrategyRunner;
import javafx.application.Application;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StrategyFxUi extends Application  {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(1000);
        primaryStage.setHeight(900);
        primaryStage.setTitle("Strategy");
        primaryStage.setResizable(false);//窗体大小不可改变
        primaryStage.getIcons().add(new Image("com/newpoint/ui/Image/cat.jpg"));//窗体图标
        getGroup(primaryStage);
        primaryStage.show();
    }
    private void getGroup(Stage primaryStage){
        Group group = new Group();
        VBox vBox = new VBox(10);

        HBox hBoxSymbol = new HBox();
        Label labelSymbol = new Label("Symbol");
        labelSymbol.setStyle("-fx-font-weight: bold;-fx-pref-width: 130;-fx-padding:5 0 0 10;");
        TextField textSymbol = new TextField("");
        textSymbol.setPrefWidth(200);
        hBoxSymbol.getChildren().addAll(labelSymbol,textSymbol);
        HBox hBoxSymbolType = new HBox();
        Label labelSymbolType = new Label("Security Type");
        labelSymbolType.setStyle("-fx-font-weight: bold;-fx-pref-width: 130;-fx-padding:5 0 0 10;");
        ComboBox comboBoxSecurityType = new ComboBox(FXCollections.observableArrayList(SecurityType.values()));
        comboBoxSecurityType.setPrefWidth(200);
        comboBoxSecurityType.setValue(SecurityType.STK);
        hBoxSymbolType.getChildren().addAll(labelSymbolType,comboBoxSecurityType);

        HBox hBoxDate = new HBox();
        Label labelStart = new Label("Start Date");
        labelStart.setStyle("-fx-font-weight: bold;-fx-pref-width: 130;-fx-padding:5 0 0 10;");
        DatePicker startDate = new DatePicker();

        startDate.setPrefWidth(200);
        Label labelEnd = new Label("End Date");
        labelEnd.setStyle("-fx-font-weight: bold;-fx-pref-width: 130;-fx-padding:5 0 0 40;");
        DatePicker endDate = new DatePicker();
        startDate.setValue(LocalDate.now());
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(startDate.getValue().plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #EEEEEE;");
                        }
                    }
                };
            }
        };
        endDate.setDayCellFactory(dayCellFactory);
        endDate.setValue(startDate.getValue().plusDays(1));
        endDate.setPrefWidth(200);
        hBoxDate.getChildren().addAll(labelStart,startDate,labelEnd,endDate);

        HBox hBoxBarSize = new HBox();
        Label labelBarSize = new Label("Bar Size");
        labelBarSize.setStyle("-fx-font-weight: bold;-fx-pref-width: 130;-fx-padding:5 0 0 10;");

        ComboBox comboBoxBarSize = new ComboBox(getBarSizeList());
        comboBoxBarSize.setValue(BarSize.ONE_DAY);
        comboBoxBarSize.setPrefWidth(200);
        hBoxBarSize.getChildren().addAll(labelBarSize,comboBoxBarSize);

        HBox hBoxNumOfPeriod = new HBox();
        Label labelNumOfPeriod = new Label(BollingerStrategy.NUM_OF_PERIOD);
        labelNumOfPeriod.setStyle("-fx-font-weight: bold;-fx-pref-width: 280;-fx-padding:5 0 0 10;");
        TextField textNumOfPeriod= new TextField("20");
        textNumOfPeriod.setPrefWidth(100);
        hBoxNumOfPeriod.getChildren().addAll(labelNumOfPeriod,textNumOfPeriod);

        HBox hBoxNumOfSTDS = new HBox();
        Label labelNumOfSTDS  = new Label(BollingerStrategy.NUM_OF_STD);
        labelNumOfSTDS.setStyle("-fx-font-weight: bold;-fx-pref-width: 280;-fx-padding:5 0 0 10;");
        TextField textNumOfSTDS = new TextField("2");
        textNumOfSTDS.setPrefWidth(100);
        hBoxNumOfSTDS.getChildren().addAll(labelNumOfSTDS,textNumOfSTDS);

        HBox hBoxBound = new HBox();
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton btnHigh = new RadioButton(BollingerStrategy.BOUNCE_FROM_MIDDLE);
        btnHigh.setStyle("-fx-font-weight: bold;-fx-pref-width: 200;-fx-padding:5 0 0 10;");
        btnHigh.setToggleGroup(toggleGroup);
        btnHigh.setSelected(true);
        RadioButton btnLow = new RadioButton(BollingerStrategy.BOUNCE_FROM_LOW);
        btnLow.setStyle("-fx-font-weight: bold;-fx-pref-width: 180;-fx-padding:5 0 0 10;");
        btnLow.setToggleGroup(toggleGroup);
        hBoxBound.getChildren().addAll(btnHigh,btnLow);

        //Strategy Result 表格数据展示部分
        VBox vBoxTable = new VBox();
        vBoxTable.setSpacing(5);
        vBoxTable.setPadding(new Insets(10, 0, 0, 0));
        Label labelNumberOfProfit = new Label("Number Of Profit:");
        labelNumberOfProfit.setStyle("-fx-font-weight: bold;-fx-padding: 2 10 0 30;");
        TextField textNumberOfProfit = new TextField("");
        textNumberOfProfit.setEditable(false);
        textNumberOfProfit.setStyle("-fx-pref-width: 80;");
        Label labelNumberOfLoss = new Label("Number Of Loss:");
        labelNumberOfLoss.setStyle("-fx-font-weight: bold;-fx-padding: 2 10 0 30;");
        TextField textNumberOfLoss = new TextField("");
        textNumberOfLoss.setEditable(false);
        textNumberOfLoss.setStyle("-fx-pref-width: 80;");
        Label labelTotalPL = new Label("totalPL:");
        labelTotalPL.setStyle("-fx-font-weight: bold;-fx-padding: 2 10 0 30;");
        TextField textTotalPL = new TextField("");
        textTotalPL.setEditable(false);
        textTotalPL.setStyle("-fx-pref-width: 80;");
        Label labelWinningPercent = new Label("WinningPercent:");
        labelWinningPercent.setStyle("-fx-font-weight: bold;-fx-padding: 2 10 0 30;");
        TextField textWinningPercent = new TextField("");
        textWinningPercent.setEditable(false);
        textWinningPercent.setStyle("-fx-pref-width: 80;");
        HBox hBoxTableTop= new HBox();
        hBoxTableTop.getChildren().addAll(labelNumberOfProfit,textNumberOfProfit,labelNumberOfLoss,textNumberOfLoss,labelTotalPL,textTotalPL,labelWinningPercent,textWinningPercent);

        TableView table = new TableView();
        VBox.setMargin(table,new Insets(0,0,0,30));
        table.setPrefHeight(450);
        table.setPrefWidth(900);
        double columnWidth = (table.getPrefWidth()-4)/4; //表格的列宽
        Label labelResult = new Label("Strategy Result");
        labelResult.setStyle("-fx-font-weight: bold;-fx-padding:5 0 0 30;-fx-font-size: 20px;");

//        table.setEditable(false);
        TableColumn sideCol = new TableColumn("Side");
        sideCol.setStyle("-fx-alignment: CENTER;");
        sideCol.setCellValueFactory(new MapValueFactory<>("side"));

        sideCol.setPrefWidth(columnWidth);
        TableColumn timeCol = new TableColumn("Time");
        timeCol.setStyle("-fx-alignment: CENTER;");
        timeCol.setCellValueFactory(new MapValueFactory<>("time"));
        timeCol.setPrefWidth(columnWidth);
        TableColumn sharesCol = new TableColumn("Shares");
        sharesCol.setStyle("-fx-alignment: CENTER;");
        sharesCol.setCellValueFactory(new MapValueFactory<>("shares"));
        sharesCol.setPrefWidth(columnWidth);
        TableColumn priceCol = new TableColumn("Price");
        priceCol.setStyle("-fx-alignment: CENTER;");
        priceCol.setCellValueFactory(new MapValueFactory<>("price"));
        priceCol.setPrefWidth(columnWidth);
        table.getColumns().addAll(sideCol, timeCol, sharesCol,priceCol);

        vBoxTable.getChildren().addAll(labelResult,hBoxTableTop, table);

        HBox hBoxBtn = new HBox();
        Button btnOk = new Button("OK");
        btnOk.setOnAction(o->{
            table.setEditable(true);
            String symbol = textSymbol.getText();
            LocalDate startDateValue = startDate.getValue();
            Date sDate = getLocalDateToDate(startDateValue);
            LocalDate endDateValue = endDate.getValue();
            Date eDate = getLocalDateToDate(endDateValue);
            int numOfPeriod =Integer.parseInt(textNumOfPeriod.getText());
            int numOfSTD = Integer.parseInt(textNumOfSTDS.getText());
            boolean bounceFromMiddle = btnHigh.selectedProperty().getValue();

            Security security = InstrumentManager.getInstance().createSecurity((SecurityType)comboBoxSecurityType.getValue(),symbol);
            MarketDataBlock marketDataBlock = MarketDataProvider.getInstance().getMarketDataBlock(security,comboBoxBarSize.getValue().toString(),sDate,eDate);
            if (marketDataBlock != null){
                BollingerStrategy bollingerStrategy = new BollingerStrategy(numOfSTD,numOfPeriod,bounceFromMiddle);
                StrategyRunner strategyRunner = new StrategyRunner(bollingerStrategy);
                strategyRunner.setMarketData(marketDataBlock);
                StrategyResult strategyResult = strategyRunner.runStrategy();
                strategyResult.calculateSummary();
                textNumberOfProfit.setText(""+strategyResult.getNumberOfProfit());
                textNumberOfLoss.setText(""+strategyResult.getNumberOfLoss());
                textTotalPL.setText(""+Math.round(strategyResult.getTotalPL() * 100) / 100.0);
                textWinningPercent.setText(""+strategyResult.getWinningPercent());
                List<Transaction> transactionList = strategyResult.getTransactions();
                List<Map<String,Object>> mapList = new ArrayList<>();
                if(transactionList != null && transactionList.size() > 0){
                    for (Transaction tra : transactionList) {
                        Map<String,Object> map = new HashMap<>();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String timeStr = simpleDateFormat.format(tra.getTime());
                        map.put("side",tra.getSide());
                        map.put("time",timeStr);
                        map.put("shares",tra.getShares());
                        map.put("price",tra.getPrice());
                        mapList.add(map);
                    }
                }

                ObservableList observableList = FXCollections.observableArrayList(mapList);
                table.setItems(observableList);
            }

        });
        HBox.setMargin(btnOk,new Insets(0,50,0,50));
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(o->{
            System.out.println("取消");
        });
        hBoxBtn.getChildren().addAll(btnOk,btnCancel);
        vBox.getChildren().addAll(hBoxSymbol,hBoxSymbolType,hBoxDate,hBoxBarSize,hBoxNumOfPeriod,hBoxNumOfSTDS,hBoxBound,hBoxBtn,vBoxTable);
        group.getChildren().add(vBox);
        Scene scene = new Scene(group);
        primaryStage.setScene(scene);
    }

    //localDate 类型转 Date 类型方法
    private static Date getLocalDateToDate(LocalDate localDate){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }
    //BarSize类存入List中
    private ObservableList getBarSizeList(){
        ObservableList barSizeList = FXCollections.observableArrayList(
           BarSize.ONE_SECOND, BarSize.FIVE_SECONDS, BarSize.TEN_SECONDS, BarSize.FIFTEEN_SECONDS, BarSize.THIRTY_SECONDS,
           BarSize.ONE_MINUTE,BarSize.TWO_MINUTES, BarSize.THREE_MINUTES,BarSize.FIVE_MINUTES, BarSize.TEN_MINUTES,
           BarSize.FIFTEEN_MINUTES,BarSize.TWENTY_MINUTES,BarSize.THIRTY_MINUTE,BarSize.ONE_HOUR, BarSize.TWO_HOURS, BarSize.THREE_HOURS,
           BarSize.FOUR_HOURS, BarSize.EIGHT_HOURS, BarSize.ONE_DAY,BarSize.ONE_WEEK,BarSize.ONE_MONTH);
        return barSizeList;
    }
}
