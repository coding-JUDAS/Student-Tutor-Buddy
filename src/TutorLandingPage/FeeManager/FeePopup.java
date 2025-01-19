package TutorLandingPage.FeeManager;

import Users.Tutor;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Popup;

public class FeePopup extends Popup {
    //Fields
    private HBox topBox;

    private String message;
    private Double anchorX, anchorY;
    private Label cur;
    private TextField amount;
    private Label setFeeLbl;
    private Button close;
    private Button update;

    public FeePopup(String message, Tutor tutor){
        super();
        this.message = message;
        this.tutor = tutor;
        createLayout();

        setBehaviour();
    }

    protected  void createLayout(){
        StackPane stackPane = new StackPane();

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        shadow.setColor(new Color(0,0,0,0.5));
        stackPane.setEffect(shadow);

        Label lblTitle = new Label("Fee popup");
        topBox = new HBox();
        topBox.setPadding(new Insets(5));
        topBox.getChildren().add(lblTitle);
        Stop[] stops = new Stop[]{
                new Stop(0, Color.DARKGREY),
                new Stop(1, Color.BLACK)
        };
        LinearGradient linearGradient = new LinearGradient(0, 0, 0,1, true, CycleMethod.REPEAT, stops);
        BackgroundFill titleFill = new BackgroundFill(linearGradient, CornerRadii.EMPTY, Insets.EMPTY);
        topBox.setBackground(new Background(titleFill));
        lblTitle.setTextFill(Color.WHITE);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBox);
        borderPane.setPadding(new Insets(5));
        borderPane.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                new CornerRadii(5),
                new BorderWidths(2)
        )));

        Label lblMessage = new Label(message);
        AnchorPane center = new AnchorPane();
        cur = new Label("Current fee = R" + tutor.getHourlyRate());
        setFeeLbl = new Label("Set Fee: ");
        amount = new TextField();
        HBox fees = new HBox();
        fees.getChildren().addAll(setFeeLbl, amount);
        center.getChildren().addAll(cur,
                                    fees);

        borderPane.setCenter(center);

        update = new Button("UPDATE");
        close = new Button("CLOSE");
        HBox bottom = new HBox();
        bottom.getChildren().addAll(close, update);
    }
    Tutor tutor = null;
    protected void setBehaviour(){
        update.setOnMouseClicked(event -> {
            tutor.setHourlyRate(Double.parseDouble(amount.getText()));
            cur.setText("Current fee: R" + amount.getText());
        });
        close.setOnMouseClicked(event -> {
            hide();
        });
    }
}
