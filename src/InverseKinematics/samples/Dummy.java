/*
 * Copyright 2013 Michael Heinrichs, http://netopyr.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package InverseKinematics.samples;

import InverseKinematics.Bone;
import InverseKinematics.Skeleton;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import InverseKinematics.Skeleton;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

public class Dummy extends Application {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 800;

	@Override
	public void start(Stage stage) throws Exception {
		final Parent root = createDummy();
		root.setTranslateX(WIDTH / 2);
		root.setTranslateY(HEIGHT / 4);

		final Scene scene = new Scene(root, WIDTH, HEIGHT);
		stage.setTitle("Dummy Sample");
		stage.setScene(scene);
		stage.show();
	}

	private static Parent createDummy() {
		final Skeleton skeleton = new Skeleton();

		final Bone hook = new Bone(110, 90);
		hook.setSkeleton(skeleton);

		final Bone torso = new Bone(80, 180, 180, 180);
		torso.getContent().add(new Ellipse(40, 0, 50, 20));
		// hook.getChildren().add(torso);

		final Bone upperArm = new Bone(60, 150 - 270);

		upperArm.getContent().setAll(new Ellipse(22.5, 0, 30, 12.5));

		final Bone lowerArm = new Bone(60, -90, -135, 0);
		upperArm.getChildren().add(lowerArm);
		final Node elbow = new Circle(12.5);
		elbow.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				lowerArm.moveHead(skeleton.sceneToLocal(event.getSceneX(),
						event.getSceneY()));
			}
		});
		final Node hand = new Circle(60, 0, 12.5);
		hand.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				lowerArm.moveTail(skeleton.sceneToLocal(event.getSceneX(),
						event.getSceneY()));
			}
		});
		lowerArm.getContent().setAll(elbow, new Ellipse(30, 0, 20, 12.5), hand);

		torso.getChildren().addAll(upperArm);

		return skeleton;
	}

	public static void main(String... args) {
		launch(args);
	}

}
