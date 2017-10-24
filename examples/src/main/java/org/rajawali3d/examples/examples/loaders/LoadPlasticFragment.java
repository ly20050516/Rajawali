package org.rajawali3d.examples.examples.loaders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.cameras.OrthographicCamera;
import org.rajawali3d.examples.R;
import org.rajawali3d.examples.examples.AExampleFragment;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;

import java.util.Stack;

public class LoadPlasticFragment extends AExampleFragment {

	@Override
    public AExampleRenderer createRenderer() {
		return new LoadModelRenderer(getActivity(), this);
	}

	private final class LoadModelRenderer extends AExampleRenderer {
		private Object3D mObjectGroup;

		public LoadModelRenderer(Context context, @Nullable AExampleFragment fragment) {
			super(context, fragment);
		}

        private Stack<Vector3> createWhirl(int numSides, float scaleFactor,
                                           float centerX, float centerY, float rotAngle) {
            Stack<Vector3> points = new Stack<Vector3>();
            Vector3[] sidePoints = new Vector3[numSides];
            float rotAngleSin = (float) Math.sin(rotAngle);
            float rotAngleCos = (float) Math.cos(rotAngle);
            float a = (float) Math.PI * (1f - 2f / (float) numSides);
            float c = (float) Math.sin(a)
                    / (rotAngleSin + (float) Math.sin(a + rotAngle));

            for (int k = 0; k < numSides; k++) {
                float t = (2f * (float) k + 1f) * (float) Math.PI
                        / (float) numSides;
                sidePoints[k] = new Vector3(Math.sin(t), Math.cos(t), 0);
            }

            for (int n = 0; n < 64; n++) {
                for (int l = 0; l < numSides; l++) {
                    Vector3 p = sidePoints[l];
                    points.add(new Vector3((p.x * scaleFactor) + centerX,
                            (p.y * scaleFactor) + centerY, 8 - (n * .25f)));
                }
                for (int m = 0; m < numSides; m++) {
                    Vector3 p = sidePoints[m];
                    double z = p.x;
                    p.x = (p.x * rotAngleCos - p.y * rotAngleSin) * c;
                    p.y = (z * rotAngleSin + p.y * rotAngleCos) * c;
                }
            }

            return points;
        }

        @Override
		protected void initScene() {

            OrthographicCamera orthoCam = new OrthographicCamera();
            orthoCam.setLookAt(0,0,0);
            orthoCam.enableLookAt();
            orthoCam.setPosition(0.0,0.0,0.0);

            orthoCam.setZoom(1.5f);


            getCurrentScene().switchCamera(orthoCam);


//            Stack<Vector3> points = createWhirl(6, 6f, 0, 0, .05f);
//
//            /**
//             * A Line3D takes a Stack of <Number3D>s, thickness and a color
//             */
//            Line3D whirl = new Line3D(points, 1, 0xffffff00);
//            Material material = new Material();
//            whirl.setMaterial(material);
//            getCurrentScene().addChild(whirl);
//
//            Vector3 axis = new Vector3(2, .4f, 1);
//            axis.normalize();
//            RotateOnAxisAnimation anim = new RotateOnAxisAnimation(axis, 360);
//            anim.setDurationMilliseconds(8000);
//            anim.setRepeatMode(Animation.RepeatMode.INFINITE);
//            anim.setTransformable3D(whirl);
//            getCurrentScene().registerAnimation(anim);
//            anim.play();


//            getCurrentScene().setBackgroundColor(Color.BLUE);
//
//            DirectionalLight key = new DirectionalLight(-3,-4,-5);
//            key.setPower(2);
//            getCurrentScene().addLight(key);

			LoaderOBJ objParser = new LoaderOBJ(this, R.raw.head3d1024);

            Object3D obj = null;
			try {
                long t1 = System.currentTimeMillis();
                objParser.parse();
                Log.d("LiuTag", "initScene: " + (System.currentTimeMillis() - t1));
				mObjectGroup = objParser.getParsedObject();
				mObjectGroup.setScale(0.1f);
                Material material = new Material();
                material.useVertexColors(false);
                material.setColorInfluence(0);
                mObjectGroup.setMaterial(material);
                mObjectGroup.getMaterial().addTexture(new Texture("plastic",R.drawable.head3d1024));
				getCurrentScene().addChild(mObjectGroup);




                ArcballCamera arcball = new ArcballCamera(mContext, ((Activity)mContext).findViewById(R.id.content_frame));
                arcball.setPosition(0, 0, 40);
				arcball.setTarget(mObjectGroup);

                getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);



//                LoaderOBJ loader = new LoaderOBJ(this, R.raw.current_merged1);
//                loader.parse();
//                obj = loader.getParsedObject();
//                obj.setScale(0.0001);
//                Log.d("LiuTag", "vertices=" + obj.getGeometry().getNumVertices());
//                Material material = new Material();
//                obj.setMaterial(material);
//                getCurrentScene().addChild(obj);

			} catch (ParsingException e) {
				Log.d("LiuTag", "initScene: " + e);
				e.printStackTrace();
			} catch (ATexture.TextureException e) {
                e.printStackTrace();
            }

//            getCurrentCamera().setPosition(-40,-40,20);
//            getCurrentCamera().setLookAt(mObjectGroup.getPosition());



        }

	}

}
