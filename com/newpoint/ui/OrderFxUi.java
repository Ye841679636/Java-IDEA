package com.newpoint.ui;

import com.newpoint.instrument.SecurityType;
import com.newpoint.order.BaseOrder;
import com.newpoint.order.OrderManager;
import com.newpoint.order.OrderType;
import com.newpoint.workstation.NPTradeSession;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Arrays;

public class OrderFxUi extends Application {


    public static void main(String[] args) {
        launch();
    }

    public static void MyLaunch(){
        launch();
    }
    //继承Application需要实现的方法
    public void start(Stage primaryStage){

        primaryStage.setWidth(800);
        primaryStage.setHeight(650);
        primaryStage.setMaxWidth(800);
        primaryStage.setMaxHeight(650);
        primaryStage.setTitle("ORDER");
        primaryStage.setResizable(false);//窗体大小不可改变
        primaryStage.getIcons().add(new Image("com/newpoint/ui/Image/cat.jpg"));//窗体图标

        getGroup(primaryStage);

        primaryStage.show();
    }

    private void getGroup(Stage primaryStage){
        Group group = new Group();
        //Security Type
        Label securityTypeLabel = new Label("Security Type");
        securityTypeLabel.setLayoutX(20);
        securityTypeLabel.setLayoutY(12);
        securityTypeLabel.setStyle("-fx-font-weight: bold;");
        ComboBox securityTypeComboBox = new ComboBox(FXCollections.observableArrayList(SecurityType.values()));
        securityTypeComboBox.setValue(SecurityType.STK);
        securityTypeComboBox.setPrefWidth(200);
        securityTypeComboBox.setLayoutX(150);
        securityTypeComboBox.setLayoutY(10);
        group.getChildren().add(securityTypeLabel);
        group.getChildren().add(securityTypeComboBox);
        //symbol
        Label labelSym = new Label("Symbol");
        labelSym.setLayoutX(20);
        labelSym.setLayoutY(52);
        labelSym.setStyle("-fx-font-weight: bold;");
        TextField textSym = new TextField("");
        textSym.setPrefWidth(200);
        textSym.setLayoutX(150);
        textSym.setLayoutY(50);
        group.getChildren().add(labelSym);
        group.getChildren().add(textSym);
        //action
        Label labelAction = new Label("Action");
        labelAction.setLayoutX(20);
        labelAction.setLayoutY(92);
        labelAction.setStyle("-fx-font-weight: bold;");
        ComboBox actionComboBox = new ComboBox();
        ObservableList list = FXCollections.observableArrayList(BaseOrder.BUY,BaseOrder.SELL);
        actionComboBox.setItems(list);
        actionComboBox.getSelectionModel().select(BaseOrder.BUY);
        actionComboBox.setPrefWidth(200);
        actionComboBox.setLayoutX(150);
        actionComboBox.setLayoutY(90);
        group.getChildren().add(labelAction);
        group.getChildren().add(actionComboBox);
        //order type
        Label labelOType= new Label("Order Type");
        labelOType.setLayoutX(20);
        labelOType.setLayoutY(132);
        labelOType.setStyle("-fx-font-weight: bold;");
        ComboBox oTypeComboBox = new ComboBox(FXCollections.observableArrayList(OrderType.values()));
        oTypeComboBox.setValue(OrderType.LMT);
        oTypeComboBox.setPrefWidth(200);
        oTypeComboBox.setLayoutX(150);
        oTypeComboBox.setLayoutY(130);
        group.getChildren().add(labelOType);
        group.getChildren().add(oTypeComboBox);
        //order size
        Label labelOSize= new Label("Total Order Size");
        labelOSize.setLayoutX(20);
        labelOSize.setLayoutY(172);
        labelOSize.setStyle("-fx-font-weight: bold;");
        TextField textOSize = new TextField("");
        textOSize.setPrefWidth(200);
        textOSize.setLayoutX(150);
        textOSize.setLayoutY(170);
        group.getChildren().add(labelOSize);
        group.getChildren().add(textOSize);
        //lmtPrice
        Label labelPrice= new Label("LMT Price");
        labelPrice.setLayoutX(20);
        labelPrice.setLayoutY(212);
        labelPrice.setStyle("-fx-font-weight: bold;");
        TextField textPrice = new TextField("");
        textPrice.setPrefWidth(200);
        textPrice.setLayoutX(150);
        textPrice.setLayoutY(210);
        group.getChildren().add(labelPrice);
        group.getChildren().add(textPrice);

        //OK and CanCel Button
        Button btnOk = new Button("OK");
        btnOk.setLayoutX(50);
        btnOk.setLayoutY(260);
        btnOk.setStyle("-fx-opacity: 0.8;");
        btnOk.setMinWidth(80);
        btnOk.setMinHeight(30);
        group.getChildren().add(btnOk);
        Button btnCancel = new Button("CanCel");
        btnCancel.setLayoutX(200);
        btnCancel.setLayoutY(260);
        btnCancel.setStyle("-fx-opacity: 0.8;");
        btnCancel.setMinWidth(80);
        btnCancel.setMinHeight(30);
        group.getChildren().add(btnCancel);
        //订单类型为 BRACKET 增加的
        Label labelProfit= new Label("Profit Taker");//Stop Loss
        labelProfit.setLayoutX(20);
        labelProfit.setLayoutY(262);
        labelProfit.setStyle("-fx-font-weight: bold;");
        labelProfit.setVisible(false);
        TextField textProfit = new TextField("");
        textProfit.setPrefWidth(200);
        textProfit.setLayoutX(150);
        textProfit.setLayoutY(260);
        textProfit.setVisible(false);
        group.getChildren().add(labelProfit);
        group.getChildren().add(textProfit);

        Label labelStopLoss= new Label("Stop Loss");
        labelStopLoss.setLayoutX(20);
        labelStopLoss.setLayoutY(302);
        labelStopLoss.setStyle("-fx-font-weight: bold;");
        labelStopLoss.setVisible(false);
        TextField textStopLoss = new TextField("");
        textStopLoss.setPrefWidth(200);
        textStopLoss.setLayoutX(150);
        textStopLoss.setLayoutY(300);
        textStopLoss.setVisible(false);
        group.getChildren().add(labelStopLoss);
        group.getChildren().add(textStopLoss);

        //订单类型选项改变
        oTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                if(newValue == OrderType.BRACKET) {
                    labelProfit.setVisible(true);
                    textProfit.setVisible(true);
                    labelStopLoss.setVisible(true);
                    textStopLoss.setVisible(true);
                    btnOk.setLayoutY(350);
                    btnCancel.setLayoutY(350);
                }
                if(oldValue == OrderType.BRACKET) {
                    labelProfit.setVisible(false);
                    textProfit.setVisible(false);
                    labelStopLoss.setVisible(false);
                    textStopLoss.setVisible(false);
                    btnOk.setLayoutY(260);
                    btnCancel.setLayoutY(260);
                }
            }
        });
        //按钮事件
        btnOk.setOnAction(o ->{
            System.out.println("点击了OK按钮");
            String symbol = textSym.getText(); //股票
//            String secType = securityTypeComboBox.getValue().toString();
//            String action = actionComboBox.getValue().toString(); //买或卖
//            String orderType = oTypeComboBox.getValue().toString();
            double quantity = Double.valueOf(textOSize.getText().isEmpty() ? "0" :textOSize.getText()); //多少股
            double lmtPrice = Double.valueOf(textPrice.getText().isEmpty() ? "0" :textPrice.getText()); //每股多少钱

            OrderManager orderManager = OrderManager.getInstance();
            BaseOrder baseOrder = null;
            if(OrderType.LMT == oTypeComboBox.getValue()){
                System.out.println("123");
                //baseOrder = orderManager.createOrder((SecurityType)securityTypeComboBox.getValue(), (OrderType) oTypeComboBox.getValue(),actionComboBox.getValue().toString(), symbol, quantity, lmtPrice);
            }else if(OrderType.BRACKET == oTypeComboBox.getValue()) {
                System.out.println("456");
                //baseOrder = orderManager.createOrder(SecurityType.valueOf(secType), OrderType.valueOf(orderType),action, symbol, quantity, lmtPrice);
            }
            if(baseOrder != null) {
//                NPTradeSession session = new NPTradeSession();
//                session.start();
//
//                System.out.println(session);
//                orderManager.placeOrder(session, baseOrder);
//                session.close();
//                System.out.println(NPTradeStation.getSession());
//                orderManager.placeOrder(NPTradeStation.getSession(), baseOrder);
            }
        });
        btnCancel.setOnAction(o ->{ //关闭
            primaryStage.close();
        });
        Scene scene = new Scene(group);
        primaryStage.setScene(scene);
    }

}
