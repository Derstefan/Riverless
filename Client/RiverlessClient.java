package Client;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import Actions.Action;
import Actions.Movement;
import Net.TestClient;
import Units.Player;
import Units.Unit;

public class Main extends BasicGame {
	long serverTime = System.currentTimeMillis();
	String modus = "area";
	Player player;
	ArrayList<Unit> units = new ArrayList<Unit>();
	// Image img;
	int mouseX, mouseY;
	// Area

	boolean inArea = true;
	// Map
	TiledMap map;

	public boolean temp = true;

	public Main() {
		super("Riverless");

	}

	public static void main(String[] arguments) throws IOException {
		System.setProperty("org.lwjgl.librarypath",
				new File(new File(System.getProperty("user.dir"), "native"),
						LWJGLUtil.getPlatformName()).getAbsolutePath());

		String name = "Stefan";
		String pw = "pups";

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("RiVERLESS----------\nLogIn:\nUser: ");
		//name = br.readLine();
		System.out.print("Passwort:");
		//pw = br.readLine();

		TestClient networkManager = new TestClient();
		networkManager.login(name, pw);
		//while (!networkManager.logedIn) {
			//System.out.print(".");
		//}
		System.out.println("Login erfolgreich !!!!!!!!!");
			try {

				// networkManager.login("Stefan", "pups");
				AppGameContainer app = new AppGameContainer(new Main());
				app.setDisplayMode(800, 600, false);
				app.start();

			} catch (SlickException e) {
				e.printStackTrace();
			}
		
	}

	@Override
	public void init(GameContainer container) throws SlickException {

		player = new Player("ich", 0, 400, 400, 600, 600, 16717611);
		changeModus("area");

		Unit unit1 = new Unit("ich", 0, 300, 300, 400, 400, 721151);
		
		units.add(unit1);
		unit1.dx = 0.05f;
		unit1.moving = true;
		unit1.inArea = true;
		Unit unit2 = new Unit("ich", 0, 300, 350, 400, 400, 784939);
		units.add(unit2);
		unit2.dx = 0.05f;
		unit2.moving = true;
		unit2.inArea = true;
		Unit unit3 = new Unit("ich", 0, 310, 350, 400, 400, 772351);
		units.add(unit3);
		unit3.dy = 0.05f;
		unit3.moving = true;
		unit3.inArea = true;

	}

	public void update(GameContainer container, int delta)
			throws SlickException {

		if (modus == "area") {
			updateArea(container, delta);
		} else if (modus == "map") {
			updateMap(container, delta);

		}
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (modus == "area") {
			renderArea(container, g);
		} else if (modus == "map") {
			renderMap(container, g);
		}

	}

	public void updateArea(GameContainer container, int delta)
			throws SlickException {

		// bsp
		// units.get(0).walkArea(0.05f, 0.0f, delta);
		// get Actionslist from testClient
		//
		updateUnitsArea(delta);

		mouseX = container.getInput().getMouseX();
		mouseY = container.getInput().getMouseY();

		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_W)) {
			Movement m = new Movement(player.areaX, player.areaY, 0.0f, -0.05f,
					serverTime);
			// an den server senden
			player.walkArea(0.0f, -0.05f, delta);

			player.yourColor = player.yourOrgColor;

		} else if (input.isKeyDown(Input.KEY_S)) {

			player.walkArea(0.0f, 0.05f, delta);

			player.yourColor = player.yourOrgColor;

		} else if (input.isKeyDown(Input.KEY_A)) {

			player.walkArea(-0.05f, 0.0f, delta);

			player.yourColor = player.yourOrgColor;
			// }
		} else if (input.isKeyDown(Input.KEY_D)) {

			player.walkArea(0.05f, 0.0f, delta);

			player.yourColor = player.yourOrgColor;

		} else if (input.isKeyDown(Input.KEY_SPACE)) {

			// beispiel !!!!!!!!!
			player.yourColor = 0;
			if (temp) {
				Movement m = new Movement(units.get(0).areaX,
						units.get(0).areaY, 0.0f, -0.05f, serverTime - 1000);
				// System.out.println("soso: " + m.time);
				units.get(0).goAction(m, serverTime);
				temp = false;
			}

		} else if (input.isKeyDown(Input.KEY_ESCAPE)) {
			container.exit();
		} else if (input.isKeyDown(Input.KEY_R)) {
			units.clear();
			// server send leaving
			// close UserList
			changeModus("map");
		} else {

			// System.out.println(0.1f*delta);
		}

	}

	// noch nicht angepasst
	public void updateMap(GameContainer container, int delta)
			throws SlickException {
		mouseX = container.getInput().getMouseX();
		mouseY = container.getInput().getMouseY();

		// move units

		// units.get(0).walkArea(0.03f, 0.02f, delta);
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_W)) {

			player.walkMap(0.0f, -0.05f, delta);

			player.yourColor = player.yourOrgColor;

		} else if (input.isKeyDown(Input.KEY_S)) {

			player.walkMap(0.0f, 0.05f, delta);

			player.yourColor = player.yourOrgColor;

		} else if (input.isKeyDown(Input.KEY_A)) {

			player.walkMap(-0.05f, 0.0f, delta);

			player.yourColor = player.yourOrgColor;

		} else if (input.isKeyDown(Input.KEY_D)) {

			player.walkMap(0.05f, 0.0f, delta);

			player.yourColor = player.yourOrgColor;

		} else if (input.isKeyDown(Input.KEY_SPACE)) {

			player.yourColor = 0;

		} else if (input.isKeyDown(Input.KEY_ESCAPE)) {
			container.exit();
		} else if (input.isKeyDown(Input.KEY_E)) {
			changeModus("area");
		} else {
			// animation.update(0);
			// System.out.println(0.1f*delta);
		}

	}

	public void renderArea(GameContainer container, Graphics g) {

		map.render((int) (300 - player.areaX), (int) (300 - player.areaY));
		map.setTileId(0, 0, 0, 2);
		// g.drawString(mouseX + " " + mouseY + " winkel:" + (int) rotated+
		// " getangle: " + (int) winkel, 100, 10);

		g.setColor(new Color(player.yourColor));
		g.fillOval(300, 300, 20, 20);

		// units

		// g.setColor(units.get(1).yourColor);
		// g.fillOval(units.get(1).areaX + (300 - player.areaX),
		// units.get(1).areaY+(300 - player.areaY), 20, 20);

		renderUnitsArea(g);
	}

	public void renderMap(GameContainer container, Graphics g) {

	//System.out.println(" " + (int) (300 - player.mapX) + "  ,  "
		//		+ (int) (300 - player.mapY));
		map.render((int) (300 - player.mapX), (int) (300 - player.mapY));
		// g.drawString(mouseX + " " + mouseY + " winkel:" + (int) rotated +
		// " getangle: " + (int) winkel, 100, 10);

		g.setColor(new Color(player.yourColor));
		g.fillOval(300, 300, 10, 10);
	}

	public void changeModus(String newModus) throws SlickException {
		if (newModus == "area") {
			player.inArea = true;
			modus = "area";

			// wird noch verändert
			// server nach daten fragen (map,userliste,...)
			map = new TiledMap("maps/desert2.tmx", "maps");
			player.areaY = (float) (map.getHeight() * map.getTileHeight() * Math
					.random());
			player.areaX = (float) (map.getWidth() * map.getTileWidth() * Math
					.random());

		} else if (newModus == "map") {
			modus = "map";
			map = new TiledMap("maps/desert3.tmx", "maps");
			player.inArea = false;
			// server nach daten fragen

		}
	}

	public void updateUnitsArea(int delta) {
		for (int i = 0; i < units.size(); i++) {

			if (units.get(i).moving) {
				units.get(i).walkArea(units.get(i).dx, units.get(i).dy, delta);
				// System.out.println(units.get(i).dx);
			}
		}
	}

	public void updateUnitsMap() {
		for (int i = 0; i < units.size(); i++) {

		}
	}

	public void renderUnitsArea(Graphics g) {
		for (int i = 0; i < units.size(); i++) {
			g.setColor(new Color(units.get(i).yourColor));
			g.fillOval(units.get(i).areaX + (300 - player.areaX),
					units.get(i).areaY + (300 - player.areaY), 20, 20);
		}
	}

	public void renderUnitsMap() {
		for (int i = 0; i < units.size(); i++) {

		}
	}
	
	
	
	
	public Color getColor(int r ,int g, int b){
		Color c = new Color(r,g,b);
		return c;
	}

	// public void cangeMapdata...

}