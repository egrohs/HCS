package hcs;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hcs.Carta.CLASS;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
	static Stage stage;
	// private static SynergyBuilder sb;
	private static Sinergias s;
	public static Set<Carta> searched = new HashSet<Carta>();
	private static VBox box;
	// private static TextField tf;
	// private static Spinner<Integer> ss;
	// private static ComboBox<CLASS> cb;
	private static VBox cartas;
	private static TextArea ta;
	private static CLASS opo = CLASS.NEUTRAL;

	@Override
	public void start(Stage stage) {
		App.stage = stage;
		// stage.initStyle(StageStyle.TRANSPARENT);
		ScrollPane sp = new ScrollPane();
		box = new VBox();
		sp.setContent(box);
		// tf = new TextField("entomb");
		// tf.setMaxSize(120, 20);
		// box.getChildren().add(tf);
		// RadioButton rb = new RadioButton("WAR");
		// box.getChildren().add(rb);
		// RadioButton rb1 = new RadioButton("HUN");
		// box.getChildren().add(rb1);
		// ss = new Spinner<Integer>(1, 10, 2);
		// ss.setMaxSize(50, 20);
		// box.getChildren().add(ss);
		// TODO fazer lista visivel da combo abrir maior.
		// cb = new ComboBox<CLASS>(new
		// ImmutableObservableList<CLASS>(Arrays.copyOfRange(CLASS.values(), 0,
		// 9)));
		// cb.setValue(CLASS.PRIEST);
		// box.getChildren().add(cb);
		// Button b = new Button("GO");
		// box.getChildren().add(b);
		cartas = new VBox();
		ta = new TextArea();
		box.getChildren().add(ta);
		box.getChildren().add(cartas);
		// b.setOnAction(new EventHandler<ActionEvent>() {
		// @Override
		// public void handle(ActionEvent event) {
		// calcula(null);
		// }
		// });
		Scene scene = new Scene(sp);
		scene.setFill(null);
		stage.setScene(scene);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		// set Stage boundaries to visible bounds of the main screen
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(150);
		stage.setHeight(primaryScreenBounds.getHeight());
		stage.setAlwaysOnTop(true);
		stage.show();
	}

	public static void calcula(Carta card) {
		// Necessario para evitar java.lang.IllegalStateException: Not on FX
		// application thread;
		// Outra thread atualizando a interface.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				searched.add(card);
				SinergyFromText.generateCardSynergies(card);
				// if (cb != null && cb.getValue() != null)
				{
					// ta.setText("");
					cartas.getChildren().removeAll(cartas.getChildren());
					// Carta card =
					// Universo.getCard(tf.getText().toLowerCase());
					if (card != null) {
						searched.add(card);
					}
					// TODO a classe deve ser deduzida uma �nica vez pelo log.
					if (opo == CLASS.NEUTRAL) {
						opo = Universo.whichClass(searched);
					}
					System.out.println(opo);
					// TODO como deduzir o turno atual no log?
					Set<Sinergia> sub = SinergyFromGames.provaveis(card, 10, opo);
					// TODO mesclar os 2 conjuntos
					sub.addAll(s.getSinergias(card, 10, opo));
					// int cont = (int)
					// Screen.getPrimary().getVisualBounds().getHeight() / 34 -
					// 2;
					int num = 1;
					for (Sinergia sinergia : sub) {
						StackPane stackPane = new StackPane();
						Carta c = (Carta) sinergia.e2;
						if (c == card) {
							c = (Carta) sinergia.e1;
						}
						ImageView iv = new ImageView(new Image("file:res/cards/" + c.id + ".png"));
						stackPane.getChildren().add(iv);
						StackPane.setAlignment(iv, Pos.CENTER_LEFT);
						String t = num + ") " + c.name + " f:" + sinergia.freq + " v:" + sinergia.valor;
						Rectangle r = new Rectangle(120, 10, Color.BLACK);
						// Text t2 = new Text(t);
						// t2.setFill(Color.BLACK);
						// t2.setFont(new Font(9));
						Text t1 = new Text(t);
						t1.setFill(Color.WHITE);
						t1.setFont(new Font(10));
						stackPane.getChildren().add(r);
						StackPane.setAlignment(r, Pos.CENTER_LEFT);
						stackPane.getChildren().add(t1);
						StackPane.setAlignment(t1, Pos.CENTER_LEFT);
						cartas.getChildren().add(stackPane);
						// cont--;
						// if (cont == 0) {
						// break;
						// }
						num++;
					}
					// ss.increment();
					// SinergyFromGames.imprimSins();
				}
				Map<Deck, Double> probs = DeckFinder.similaridade(searched);
				StringBuilder sbb = new StringBuilder();
				for (Deck k : probs.keySet()) {
					// TODO buscar a classe do opo no log?
					if (k.classe == opo) {
						sbb.append(k.nome + " = " + probs.get(k) + "%\n");
					}
				}
				ta.setText(sbb.toString());
			}
		});
	}

	public static void main(String[] args) {
		Universo.leCards();
		// SinergyFromGames.leSinergias();
		s = new Sinergias();
		new DeckFinder();
		new LogReader().start();
		// TODO detectar qndo um jogo comeca e termina para resetar as
		// variaveis.
		launch(args);
	}
}