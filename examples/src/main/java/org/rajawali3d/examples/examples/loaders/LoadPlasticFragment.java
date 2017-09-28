package org.rajawali3d.examples.examples.loaders;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.examples.R;
import org.rajawali3d.examples.examples.AExampleFragment;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.CubeMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;

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

        @Override
		protected void initScene() {


			LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),
												mTextureManager, R.raw.current_merged);
			try {

				objParser.parse();
				mObjectGroup = objParser.getParsedObject();
				mObjectGroup.setScale(0.1f);
                Material material = new Material();
                material.useVertexColors(false);
                material.setColorInfluence(0);
                mObjectGroup.setMaterial(material);
                mObjectGroup.getMaterial().addTexture(new Texture("plastic",R.drawable.current_merged));
				getCurrentScene().addChild(mObjectGroup);



                ArcballCamera arcball = new ArcballCamera(mContext, ((Activity)mContext).findViewById(R.id.content_frame));
                arcball.setPosition(0, 0, 40);
                getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);
//                getCurrentCamera().setPosition(0,0,40);

			} catch (ParsingException e) {
				e.printStackTrace();
			} catch (ATexture.TextureException e) {
                e.printStackTrace();
            }

        }

	}

}
