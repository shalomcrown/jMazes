package jmazes;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class TestJME extends SimpleApplication {

	private Geometry geom;

	@Override
	public void simpleInitApp() {
		Box b = new Box(1, 1, 1);
		geom = new Geometry("Box", b);

		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); // create a simple material
		mat.setTexture("ColorMap",
	            assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
		geom.setMaterial(mat); // set the cube's material
		rootNode.attachChild(geom);


		BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText helloText = new BitmapText(font, false);
	    helloText.setSize(1);
	    helloText.setText("Cube");

	    helloText.setLocalTranslation(1.3F, 1, 2);
	    rootNode.attachChild(helloText);
	}

	@Override
	public void simpleUpdate(float tpf) {
		geom.rotate(0,  2 * tpf, tpf);
		super.simpleUpdate(tpf);
	}

	public static void main(String[] args) {
		var test = new TestJME();

		test.start();

	}

}
