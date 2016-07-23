package com.kz.pipeCutter;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.plot3d.rendering.view.modes.ViewBoundMode;

import com.kz.pipeCutter.BBB.BBBStatus;
import com.kz.pipeCutter.BBB.commands.ExecuteMdi;
import com.kz.pipeCutter.ui.Settings;

public class MyPopupMenu extends PopupMenu {

	public MyPopupMenu() {
		MenuItem menuItem = new MenuItem("Cut whole pipe");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				CutThread th = new CutThread(true);
				th.execute();
			}
		});
		this.add(menuItem);
		this.addSeparator();

		MenuItem menuItem7 = new MenuItem("Toggle edges");
		menuItem7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SurfaceDemo.instance.NUMBER_EDGES = !SurfaceDemo.instance.NUMBER_EDGES;
				SurfaceDemo.instance.initDraw();
			}
		});
		this.add(menuItem7);

		MenuItem menuItem8 = new MenuItem("Toggle points");
		menuItem8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SurfaceDemo.instance.NUMBER_POINTS = !SurfaceDemo.instance.NUMBER_POINTS;
				SurfaceDemo.instance.initDraw();
			}
		});
		this.add(menuItem8);

		MenuItem menuItem9 = new MenuItem("Zoom point");
		menuItem9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SurfaceDemo.instance.canvas.getView()
						.setBoundManual(new BoundingBox3d(SurfaceDemo.instance.plasma.getPosition(), SurfaceDemo.zoomBounds));
				SurfaceDemo.ZOOM_POINT = true;
			}
		});
		this.add(menuItem9);

		MenuItem menuItem10 = new MenuItem("Cancel zoom");
		menuItem10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				SurfaceDemo.instance.canvas.getView().setBoundMode(ViewBoundMode.AUTO_FIT);
				SurfaceDemo.ZOOM_POINT = false;
			}
		});
		this.add(menuItem10);

		this.addSeparator();

		MenuItem menuItem6 = new MenuItem("Move on edge");
		menuItem6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (SurfaceDemo.instance.lastClickedPoint.getClass().getName().equals("com.kz.pipeCutter.MyPickablePoint")) {

					CutThread th = new CutThread(false, SurfaceDemo.instance.lastClickedPoint);
					th.execute();
				}
			}
		});
		this.add(menuItem6);
		this.addSeparator();

		MenuItem menuItem2 = new MenuItem("SMOOTHIE - Run last program");
		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SurfaceDemo.instance.smoothie.send("play /sd/prog.gcode -q");
			}
		});
		this.add(menuItem2);

		MenuItem menuItem3 = new MenuItem("SMOOTHIE - Move to selected point");
		menuItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				MyPickablePoint mp = SurfaceDemo.instance.lastClickedPoint;
				String gCode = SurfaceDemo.instance.utils.coordinateToGcode(mp.xyz);
				SurfaceDemo.instance.smoothie.send(gCode);

			}
		});
		this.add(menuItem3);

		MenuItem menuItem5 = new MenuItem("SMOOTHIE - Move on edge");
		menuItem5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (SurfaceDemo.instance.lastClickedPoint.getClass().getName().equals("com.kz.pipeCutter.MyPickablePoint")) {
					CutThread ct = new CutThread(false, SurfaceDemo.instance.lastClickedPoint);

					ct.execute();
				}

			}
		});
		this.add(menuItem5);

		MenuItem menuItem4 = new MenuItem("SMOOTHIE - move to HOME position");
		menuItem4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SurfaceDemo.instance.smoothie.send("G90");
				SurfaceDemo.instance.smoothie.send("G28 X0 Y0 Z0");
			}
		});
		this.addSeparator();
		this.add(menuItem4);

		MenuItem menuItem11 = new MenuItem("Find BBB");
		menuItem11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SurfaceDemo.instance.discoverer.discover();
			}
		});
		this.addSeparator();
		this.add(menuItem11);

		this.addSeparator();

		// Gcodes for BBB
		MenuItem menuItem12 = new MenuItem("Set point as origin - G92");
		menuItem12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						String mdiCommand = String.format("G92 X%.3f Y%.3f Z%.3f", SurfaceDemo.instance.lastClickedPoint.xyz.x,
								SurfaceDemo.instance.lastClickedPoint.xyz.y, SurfaceDemo.instance.lastClickedPoint.xyz.z);
						Settings.instance.log(mdiCommand);
						new ExecuteMdi(mdiCommand).start();
						try {
							TimeUnit.SECONDS.sleep(5);
							BBBStatus.getInstance().reSubscribeMotion();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
			}
		});
		this.add(menuItem12);

	}
}
