package com.newpoint.ui;

import com.newpoint.account.Account;
import com.newpoint.account.AccountManager;
import com.newpoint.account.Position;
import com.newpoint.instrument.Security;
import com.newpoint.strategy.Strategy;
import com.newpoint.workstation.NPTradeStation;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class AccountFxUi extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("New Point Trade Station");

       // primaryStage.setResizable(false);//窗体大小不可改变
        primaryStage.getIcons().add(new Image("com/newpoint/ui/Image/cat.jpg"));//窗体图标
        getGroup(primaryStage);
        primaryStage.show();
    }
    private void getGroup(Stage primaryStage){
        Group group = new Group();
        Label labelAccountTitle = new Label("New Point Trade Station");
        labelAccountTitle.setStyle("-fx-font-weight: bold;-fx-font-size: 20px;-fx-padding: 10px;");
        labelAccountTitle.setLayoutX(300);
        HBox hBox = new HBox();
        hBox.setLayoutX(20);
        hBox.setLayoutY(50);

        VBox vBox = new VBox();
        vBox.setPrefWidth(600);
        vBox.setPrefHeight(700);
        vBox.setStyle("-fx-border-width: 2px;-fx-border-color: #ccc;");

        Label labelAccount = new Label("Account");
        labelAccount.setStyle("-fx-font-weight: bold;-fx-padding:0 0 0 10;-fx-font-size: 20px;");
        GridPane gridPaneAccount = new GridPane();
        gridPaneAccount.setHgap(1);
        gridPaneAccount.setVgap(1);
        gridPaneAccount.setPrefWidth(400);
        gridPaneAccount.setPrefHeight(100);
        gridPaneAccount.setStyle("-fx-padding: 0 10px;");
        gridPaneAccount.setGridLinesVisible(true);//使用边框

        String colWidth = "220px";
        String colSpace = "4px 30px";
        Label labelAccounts = new Label("Accounts: ");
        labelAccounts.setStyle("-fx-font-weight: bold;-fx-padding: "+colSpace+";-fx-pref-width: "+colWidth+";-fx-alignment: center-right;-fx-background-color: aliceblue;");
        gridPaneAccount.add(labelAccounts,0,0);
        ComboBox comboBoxAccountsValue = new ComboBox();
        comboBoxAccountsValue.setStyle("-fx-font-weight: bold;-fx-padding: 0 20px;-fx-pref-width: "+colWidth+";");
        gridPaneAccount.add(comboBoxAccountsValue,1,0);
        List<String> accounts = getAccounts();
        comboBoxAccountsValue.setItems(FXCollections.observableArrayList(accounts));
        comboBoxAccountsValue.getSelectionModel().selectFirst();//默认选中第一个

        Label netValueLabel = new Label("NetValue: ");
        gridPaneAccount.add(netValueLabel,0,1);
        netValueLabel.setStyle("-fx-font-weight: bold;-fx-padding: "+colSpace+";-fx-pref-width: "+colWidth+";-fx-alignment: center-right;-fx-background-color: aliceblue");
        TextField netValueText = new TextField("");
        netValueText.setEditable(false);
        gridPaneAccount.add(netValueText,1,1);
        netValueText.setStyle("-fx-font-weight: bold;-fx-padding: "+colSpace+";-fx-pref-width: "+colWidth+";");

        Label cashValueLabel = new Label("CashValue: ");
        gridPaneAccount.add(cashValueLabel,0,2);
        cashValueLabel.setStyle("-fx-font-weight: bold;-fx-padding: "+colSpace+";-fx-pref-width: "+colWidth+";-fx-alignment: center-right;-fx-background-color: aliceblue");
        TextField cashValueText = new TextField("");
        cashValueText.setEditable(false);
        gridPaneAccount.add(cashValueText,1,2);
        cashValueText.setStyle("-fx-font-weight: bold;-fx-padding: "+colSpace+";-fx-pref-width: "+colWidth+";");

        Label buyingPowerLabel = new Label("BuyingPower: ");
        gridPaneAccount.add(buyingPowerLabel,0,3);
        buyingPowerLabel.setStyle("-fx-font-weight: bold;-fx-padding: "+colSpace+";-fx-pref-width: "+colWidth+";-fx-alignment: center-right;-fx-background-color: aliceblue");
        TextField buyingPowerText = new TextField("");
        buyingPowerText.setEditable(false);
        gridPaneAccount.add(buyingPowerText,1,3);
        buyingPowerText.setStyle("-fx-font-weight: bold;-fx-padding: "+colSpace+";-fx-pref-width: "+colWidth+";");

        Label previousDayBalLabel = new Label("PreviousDayBal: ");
        gridPaneAccount.add(previousDayBalLabel,0,4);
        previousDayBalLabel.setStyle("-fx-font-weight: bold;-fx-padding: "+colSpace+";-fx-pref-width: "+colWidth+";-fx-alignment: center-right;-fx-background-color: aliceblue");
        TextField previousDayBalText = new TextField("");
        previousDayBalText.setEditable(false);
        gridPaneAccount.add(previousDayBalText,1,4);
        previousDayBalText.setStyle("-fx-font-weight: bold;-fx-padding: "+colSpace+";-fx-pref-width: "+colWidth+";");

        Label labelPosition = new Label("Position");
        labelPosition.setStyle("-fx-font-weight: bold;-fx-padding:5 0 0 10;-fx-font-size: 20px;");
        TableView positionTable = new TableView();
        positionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);//表格列宽自适应
        VBox.setMargin(positionTable,new Insets(0,10,10,10));
        positionTable.setPrefHeight(450);
        positionTable.setPrefWidth(570);
        TableColumn symbolCol = new TableColumn("Symbol");
        symbolCol.setStyle("-fx-alignment: CENTER;");
        symbolCol.setCellValueFactory(new MapValueFactory<>("symbol"));
        TableColumn quantityCol = new TableColumn("Quantity");
        quantityCol.setStyle("-fx-alignment: CENTER;");
        quantityCol.setCellValueFactory(new MapValueFactory<>("quantity"));
        TableColumn averageCostCol = new TableColumn("AverageCost");
        averageCostCol.setStyle("-fx-alignment: CENTER;");
        averageCostCol.setCellValueFactory(new MapValueFactory<>("averageCost"));
        TableColumn realizedPLCol = new TableColumn("RealizedPL");
        realizedPLCol.setStyle("-fx-alignment: CENTER;");
        realizedPLCol.setCellValueFactory(new MapValueFactory<>("realizedPL"));
        TableColumn unrealizedPLCol = new TableColumn("UnrealizedPL");
        unrealizedPLCol.setStyle("-fx-alignment: CENTER;");
        unrealizedPLCol.setCellValueFactory(new MapValueFactory<>("unrealizedPL"));
        TableColumn marketValueCol = new TableColumn("MarketValue");
        marketValueCol.setStyle("-fx-alignment: CENTER;");
        marketValueCol.setCellValueFactory(new MapValueFactory<>("marketValue"));
        TableColumn marketPriceCol = new TableColumn("MarketPrice");
        marketPriceCol.setStyle("-fx-alignment: CENTER;");
        marketPriceCol.setCellValueFactory(new MapValueFactory<>("marketPrice"));
        positionTable.getColumns().addAll(symbolCol,quantityCol,averageCostCol,marketValueCol,marketPriceCol);
        // Accounts 改变事件
        comboBoxAccountsValue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Account account = getAccount(newValue+"");
                if(account != null){
                    netValueText.setText(account.getNetValue()+"");
                    cashValueText.setText(account.getCashValue()+"");
                    buyingPowerText.setText(account.getBuyingPower()+"");
                    previousDayBalText.setText(account.getPreviousDayBal()+"");
                    ObservableList observableList = getPosition(account);
                    if(observableList != null && observableList.size() >0){
                        positionTable.setItems(observableList);
                    }else{
                        positionTable.setItems(null);
                    }
                }else{
                    netValueText.setText("");
                    cashValueText.setText("");
                    buyingPowerText.setText("");
                    previousDayBalText.setText("");
                    positionTable.setItems(null);
                }
            }
        });
        Account account = getAccount(comboBoxAccountsValue.getValue().toString());
        if(account != null){
            netValueText.setText(account.getNetValue()+"");
            cashValueText.setText(account.getCashValue()+"");
            buyingPowerText.setText(account.getBuyingPower()+"");
            previousDayBalText.setText(account.getPreviousDayBal()+"");
            ObservableList observableList = getPosition(account);
            positionTable.setItems(observableList);
        }
        vBox.getChildren().addAll(labelAccount,gridPaneAccount,labelPosition,positionTable);
        //右边
        VBox vBoxRight = new VBox();
        vBoxRight.setStyle("-fx-border-width: 2px;-fx-border-color: #ccc;");
        Label labelButton = new Label("Operation Button");
        labelButton.setStyle("-fx-font-weight: bold;-fx-padding:5 0 0 10;-fx-font-size: 20px;");
        Button strategyButton = new Button("Strategy");
        VBox.setMargin(strategyButton,new Insets(5));
        strategyButton.setStyle("-fx-font-weight: bold;");
        strategyButton.setOnAction(o ->{
            Stage stageStrategy = new Stage();
           // stageStrategy.
        });
        vBoxRight.getChildren().addAll(labelButton,strategyButton);
        hBox.getChildren().addAll(vBox,vBoxRight);
        group.getChildren().addAll(labelAccountTitle,hBox);
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double vboxWidth = newVal.doubleValue()*0.6;
            double vBoxRightWidth = newVal.doubleValue()*0.35;
            vBox.setPrefWidth(vboxWidth);
            vBoxRight.setPrefWidth(vBoxRightWidth);
        });
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double vboxHeight = newVal.doubleValue()*0.8;
            vBox.setPrefHeight(vboxHeight);
            vBoxRight.setPrefHeight(vboxHeight);
        });
        Scene scene = new Scene(group,1000,800);
        primaryStage.setScene(scene);
    }

    //存在的所有账号
    private List<String> getAccounts() {
        List<String> list = new ArrayList<>();
        Collection<Account> accounts = AccountManager.getInstance().getAccounts();
        if(accounts != null && accounts.size() > 0){
            for (Account account:accounts) {
                list.add(account.getAccountKey());
                list.add("2");
            }
        }
        return list;
    }

    //获取对应账户
    private Account getAccount(String accountKey){
        Account account = AccountManager.getInstance().getAccount(accountKey);
        return account;
    }
    //加载账户对应股票信息
    private ObservableList getPosition(Account account){
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(account.getPositions() != null && account.getPositions().size() > 0) {
            Iterator iterator = account.getPositions().iterator();
            while (iterator.hasNext()) {
                Position position = (Position) iterator.next();
                Map<String, Object> map = new HashMap<>();
                Security security = position.getInstrument();
                map.put("symbol", security.getSymbol());
                map.put("quantity", position.getQuantity());
                map.put("averageCost", position.getAverageCost());
                map.put("marketValue", position.getMarketValue());
                map.put("marketPrice", position.getMarketPrice());
                mapList.add(map);
            }
        }
        ObservableList observableList = FXCollections.observableArrayList(mapList);
        return observableList;
    }
}
