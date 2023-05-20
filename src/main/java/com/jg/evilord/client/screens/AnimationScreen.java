package com.jg.evilord.client.screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import com.jg.evilord.animation.Animation;
import com.jg.evilord.animation.Keyframe;
import com.jg.evilord.animation.RepetitiveAnimation;
import com.jg.evilord.animation.Transform;
import com.jg.evilord.animation.model.JgModel;
import com.jg.evilord.animation.model.JgModelPart;
import com.jg.evilord.client.handler.ClientsHandler;
import com.jg.evilord.client.screens.widgets.JGSelectionList;
import com.jg.evilord.client.screens.widgets.JGSelectionList.Key;
import com.jg.evilord.client.screens.widgets.JgEditBox;
import com.jg.evilord.client.screens.widgets.JgPartKey;
import com.jg.evilord.client.screens.widgets.KeyframeLineWidget;
import com.jg.evilord.serializers.animation.AnimationSerializer;
import com.jg.evilord.utils.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.CycleButton.OnValueChange;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AnimationScreen extends Screen {

	public static ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");

	private ModelPartsScreen gunPartScreen;
	private final List<Button> buttons;
	private final List<EditBox> edits;
	private final List<OptionsList> options;
	private final List<CycleButton<Integer>> integerCycles;
	private final List<CycleButton<Boolean>> booleanCycles;
	private final List<Integer> cycles;
	List<Pair<JgModelPart, float[]>> transforms;
	// private AnimationSelectionList list;
	private final JgModel model;

	KeyframeLineWidget keyframeLine;
	JGSelectionList modelPartList;
	JGSelectionList posList;
	JGSelectionList rotList;

	private int i;
	private int j;

	private int minX, maxX, deltaX;
	private int scrollMax, scale;
	private int current;
	
	private boolean cycleStarted;
	private boolean ctrl;
	private boolean start;
	private boolean rot;
	private float prog;
	private float prev;
	private float MAX = 4f;
	private boolean[] keys;
	private boolean init;
	private boolean keyframe;

	private Keyframe kf;

	public AnimationScreen(JgModel model, ModelPartsScreen screen) {
		super(new TranslatableComponent("Animation Screen"));
		this.gunPartScreen = screen;
		this.buttons = new ArrayList<>();
		this.edits = new ArrayList<>();
		this.options = new ArrayList<>();
		this.integerCycles = new ArrayList<>();
		this.booleanCycles = new ArrayList<>();
		this.cycles = new ArrayList<>();
		this.transforms = new ArrayList<>();
		this.model = model;
		this.keyframe = true;
		this.i = width / 2;
		this.j = height / 2;
		this.keys = new boolean[400];
	}

	@Override
	protected void init() {
		super.init();
		// if(!init) {

		edits.clear();
		buttons.clear();
		booleanCycles.clear();

		posList = new JGSelectionList(new JGSelectionList.Key[0], this, this.font, 202, 2, 60, 14, 4, (k, i) -> {
			if (model.getAnimation() != null) {
				if(keyframeLine.getSelected() != -1) {
					Keyframe kf = model.getAnimation().getKeyframes().get(keyframeLine
							.getSelected());
					JgModelPart part = Utils.getPartByName(model, k.getKey());
					if(kf.translations.containsKey(part)) {
						edits.get(0).setValue(String.valueOf(kf.translations.get(part)[0]));
						edits.get(1).setValue(String.valueOf(kf.translations.get(part)[1]));
						edits.get(2).setValue(String.valueOf(kf.translations.get(part)[2]));
						LogUtils.getLogger().info("key: " + k.getKey());
					} else {
						LogUtils.getLogger().info("key: " + k.getKey() + 
								" doesnt have a transform");
					}
				}
			}
		});
		rotList = new JGSelectionList(new JGSelectionList.Key[0], this, this.font, 273, 2, 60, 14, 4, (k, i) -> {
			if (model.getAnimation() != null) {
				if(keyframeLine.getSelected() != -1) {
					Keyframe kf = model.getAnimation().getKeyframes().get(keyframeLine
							.getSelected());
					JgModelPart part = Utils.getPartByName(model, k.getKey());
					if(kf.rotations.containsKey(part)) {
						edits.get(3).setValue(String.valueOf(kf.rotations.get(part)[0]));
						edits.get(4).setValue(String.valueOf(kf.rotations.get(part)[1]));
						edits.get(5).setValue(String.valueOf(kf.rotations.get(part)[2]));
					}
				}
			}
		});

		JgPartKey[] modelParts = new JgPartKey[model.getPartsAsList().size()];
		for (int i = 0; i < model.getPartsAsList().size(); i++) {
			modelParts[i] = new JgPartKey(font, model.getPartsAsList().get(i));
		}
		modelPartList = new JGSelectionList(modelParts, this, this.font, 100, 2, 60, 14, 4, (k, i) -> {

		});

		keyframeLine = new KeyframeLineWidget(posList, rotList, font, model, this);

		booleanCycles.add(buildBooleanCycle((s) -> {
			if (s) {
				return new TranslatableComponent("R");
			} else {
				return new TranslatableComponent("T");
			}
		}, false, 335, 2, 30, 20, new TranslatableComponent(""), (c, v) -> {
			rot = v;
		}));
		
		booleanCycles.add(buildBooleanCycle((s) -> {
			if (s) {
				return new TranslatableComponent("K");
			} else {
				return new TranslatableComponent("T");
			}
		}, false, 100, 166, 60, 20, new TranslatableComponent("Keyframe"), (c, v) -> {
			keyframe = v;
		}));
		
		booleanCycles.add(buildBooleanCycle((s) -> {
			if (s) {
				return new TranslatableComponent("T");
			} else {
				return new TranslatableComponent("F");
			}
		}, false, 100, 210, 60, 20, new TranslatableComponent("Loop"), (c, v) -> {
			ClientsHandler.getClient(Minecraft.getInstance().getUser()).loop = v;
		}));
		booleanCycles.get(2).setValue(ClientsHandler.getClient(Minecraft.getInstance()
				.getUser()).loop);
		
		integerCycles.add(buildIntCycle((s) -> {
			if (s == 0) {
				return new TranslatableComponent("NON");
			} else if (s == 1) {
				return new TranslatableComponent("SC");
			} else if(s == 2) {
				return new TranslatableComponent("EC");
			}
			return new TranslatableComponent("NON");
		}, 0, 100, 188, 60, 20, new TranslatableComponent("Type"), (c, v) -> {
			if(model.getAnimation() != null) {
				if(keyframeLine.getSelected() != -1) {
					LogUtils.getLogger().info("v: " + v);
					if(v == 1 && !cycleStarted) {
						getSelectedKeyframe().type = v;
					} else if(v == 1 && cycleStarted) {
						LogUtils.getLogger().info("A cycle is already started");
					} else {
						getSelectedKeyframe().type = v;
					}
					if(v == 1) {
						cycleStarted = true;
					} else if(v == 2) {
						cycleStarted = false;
					}
				}
			}
		}, 0, 1, 2));

		buttons.add(new Button(372, 2, 50, 20, new TranslatableComponent("GunParts"), (b) -> {
			Minecraft.getInstance().setScreen(gunPartScreen);
		}));

		// String.valueOf(((JgPartKey)modelPartList.getSelectedKey())
		// .getPart().getTransform().pos[0])
		// Pos
		// x
		edits.add(new EditBox(font, 16, 0, 80, 20, new TranslatableComponent("animationScreen.pos.x")));
		edits.get(0).setValue("0");
		edits.get(0).setResponder((s) -> {
			try {
				/*float val = Float.parseFloat(s);
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != null) {
						setVecVal(false, 0, val);
						LogUtils.getLogger().info("here");
					}
				}*/
				if (model.getAnimation() == null)
					return;
				if (!posList.getSelectedIndexes().isEmpty()) {
					getSelectedKeyframe().translations.get(getPart(posList
							.getSelectedKey().getKey()))[0] = Float
							.parseFloat(s);
					System.out.println("Val: "
							+ getSelectedKeyframe().translations.get(getPart(
									posList.getSelectedKey().getKey()))[0]);
				}
			} catch (Exception e) {

			}
		});
		// y
		edits.add(new EditBox(font, 16, 20, 80, 20, new TranslatableComponent("animationScreen.pos.y")));
		edits.get(1).setValue("0");
		edits.get(1).setResponder((s) -> {
			try {
				/*float val = Float.parseFloat(s);
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != null) {
						LogUtils.getLogger().info("changing value");
						setVecVal(false, 1, val);
					}
				}*/
				if (model.getAnimation() == null)
					return;
				if (!posList.getSelectedIndexes().isEmpty()) {
					getSelectedKeyframe().translations.get(getPart(posList
							.getSelectedKey().getKey()))[1] = Float
							.parseFloat(s);
					System.out.println("Val: "
							+ getSelectedKeyframe().translations.get(getPart(
									posList.getSelectedKey().getKey()))[1]);
				}
			} catch (Exception e) {

			}
		});
		// z
		edits.add(new EditBox(font, 16, 40, 80, 20, new TranslatableComponent("animationScreen.pos.z")));
		edits.get(2).setValue("0");
		edits.get(2).setResponder((s) -> {
			try {
				/*float val = Float.parseFloat(s);
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != null) {
						setVecVal(false, 2, val);
					}
				}*/
				if (model.getAnimation() == null)
					return;
				if (!posList.getSelectedIndexes().isEmpty()) {
					getSelectedKeyframe().translations.get(getPart(posList
							.getSelectedKey().getKey()))[2] = Float
							.parseFloat(s);
					System.out.println("Val: "
							+ getSelectedKeyframe().translations.get(getPart(
									posList.getSelectedKey().getKey()))[2]);
				}
			} catch (Exception e) {

			}
		});

		// Rot
		// rx
		edits.add(new EditBox(font, 16, 60, 80, 20, new TranslatableComponent("animationScreen.rot.x")));
		edits.get(3).setValue("0");
		edits.get(3).setResponder((s) -> {
			try {
				/*float val = Float.parseFloat(s);
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != null) {
						setVecVal(true, 0, val);
					}
				}*/
				if (model.getAnimation() == null)
					return;
				if (!rotList.getSelectedIndexes().isEmpty()) {
					getSelectedKeyframe().rotations.get(getPart(rotList
							.getSelectedKey().getKey()))[0] = Float
							.parseFloat(s);
					System.out.println("Val: "
							+ getSelectedKeyframe().rotations.get(getPart(
									rotList.getSelectedKey().getKey()))[0]);
				}
			} catch (Exception e) {

			}
		});
		// ry
		edits.add(new EditBox(font, 16, 80, 80, 20, new TranslatableComponent("animationScreen.rot.y")));
		edits.get(4).setValue("0");
		edits.get(4).setResponder((s) -> {
			try {
				/*float val = Float.parseFloat(s);
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != null) {
						setVecVal(true, 1, val);
					}
				}*/
				if (model.getAnimation() == null)
					return;
				if (!rotList.getSelectedIndexes().isEmpty()) {
					getSelectedKeyframe().rotations.get(getPart(rotList
							.getSelectedKey().getKey()))[1] = Float
							.parseFloat(s);
					System.out.println("Val: "
							+ getSelectedKeyframe().rotations.get(getPart(
									rotList.getSelectedKey().getKey()))[1]);
				}
			} catch (Exception e) {

			}
		});
		// rz
		edits.add(new EditBox(font, 16, 100, 80, 20, new TranslatableComponent("animationScreen.rot.z")));
		edits.get(5).setValue("0");
		edits.get(5).setResponder((s) -> {
			try {
				/*float val = Float.parseFloat(s);
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != null) {
						setVecVal(true, 2, val);
					}
				}*/
				if (model.getAnimation() == null)
					return;
				if (!rotList.getSelectedIndexes().isEmpty()) {
					getSelectedKeyframe().rotations.get(getPart(rotList
							.getSelectedKey().getKey()))[2] = Float
							.parseFloat(s);
					System.out.println("Val: "
							+ getSelectedKeyframe().rotations.get(getPart(
									rotList.getSelectedKey().getKey()))[2]);
				}
			} catch (Exception e) {

			}
		});
		// File managing
		edits.add(new JgEditBox(font, 100, 59, 80, 20, new TranslatableComponent("animationScreen.file")) {

		});
		edits.get(6).setValue("");
		edits.get(6).setResponder((s) -> {

		});
		edits.add(new EditBox(font, 16, 120, 80, 20, new TranslatableComponent("animationScreen.dur")));
		edits.get(7).setValue("0");
		edits.get(7).setResponder((s) -> {
			/*
			 * if(keyframeLine.getSelected() != -1 && !edits.get(7).getValue().isBlank()) {
			 * keyframeLine.getKeyframes()[keyframeLine.getSelected()].dur =
			 * Integer.parseInt(edits.get(7).getValue()); }
			 */
			if (model.getAnimation() == null)
				return;
			if(keyframeLine.getSelected() != -1) {
				getSelectedKeyframe().dur = (int)Float.parseFloat(s.isEmpty() ? "1" 
						: s);
				Utils.updateKeyframesFromAnimation(model.getAnimation());
				keyframeLine.update(model.getAnimation());
			}
		});
		edits.add(new EditBox(font, 16, 160, 80, 20, new TranslatableComponent("animationScreen.scale")));
		edits.get(8).setValue("1");
		edits.get(8).setResponder((s) -> {
			if (!edits.get(8).getValue().isBlank()) {
				int scale = 1;
				try {
					scale = Integer.parseInt(edits.get(8).getValue());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				keyframeLine.setScale(scale);
			}
		});
		edits.add(new EditBox(font, 16, 182, 80, 20, new TranslatableComponent("animationScreen.easing")));
		edits.get(9).setValue("empty");
		edits.get(9).setResponder((s) -> {
			if (!edits.get(9).getValue().isBlank()) {
				if(keyframeLine.getSelected() != -1) {
					getSelectedKeyframe().easing = s;
				}
			}
		});
		edits.add(new EditBox(font, 16, 138, 80, 20, new TranslatableComponent("animationScreen.times")));
		edits.get(10).setValue("1");
		
		// Buttons
		buttons.add(new Button(342, 24, 30, 20, new TranslatableComponent("Play"), (b) -> {
			model.setPlayAnimation(true);
		}));
		buttons.add(new Button(374, 24, 30, 20, new TranslatableComponent("Stop"), (b) -> {
			model.setPlayAnimation(false);
		}));
		buttons.add(new Button(342, 44, 30, 20, new TranslatableComponent("Next"), (b) -> {
			if (model.getAnimation() != null) {
				model.getAnimator().nextTick();
			}
		}));
		buttons.add(new Button(374, 44, 30, 20, new TranslatableComponent("Prev"), (b) -> {
			if (model.getAnimation() != null) {
				model.getAnimator().prevTick();
			}
		}));
		buttons.add(new Button(202, 59, 30, 20, new TranslatableComponent("Update"), (b) -> {
			if(model.getAnimation() instanceof RepetitiveAnimation) {
				if(!edits.get(10).getValue().isBlank()) {
					((RepetitiveAnimation)model.getAnimation())
						.setTimes(Integer.parseInt(edits.get(10).getValue()));
				} else {
					((RepetitiveAnimation)model.getAnimation()).setTimes(1);
				}
				keyframeLine.update(model.getAnimation());
			}
		}));
		buttons.add(new Button(234, 59, 30, 20, new TranslatableComponent("Add"), (b) -> {
			if (model.getAnimation() != null) {
				if (keyframeLine.getSelected() != -1) {
					Keyframe kf = new Keyframe(4);
					Keyframe kfsel = getSelectedKeyframe();
					kf.startTick = kfsel.startTick + kfsel.dur;
					kf.startVisualTick = kf.startTick + 4;
					Utils.insertInto(keyframeLine.getSelected(), 
							model.getAnimation().getKeyframes(), kf);
				} else {
					Keyframe kf = new Keyframe(4);
					kf.startTick = 0;
					kf.startVisualTick = 4;
					model.getAnimation().getKeyframes().add(kf);
				}
				Utils.updateKeyframesFromAnimation(model.getAnimation());
				keyframeLine.update(model.getAnimation());
			}
		}));

		buttons.add(new Button(266, 59, 30, 20, new TranslatableComponent("Remove"), (b) -> {
			Key[] posKeys = posList.getSelectedKeys();
			Key[] rotKeys = rotList.getSelectedKeys();
			boolean pos = false;
			boolean rot = false;
			if (posKeys.length > 0) {
				for (Key key : posKeys) {
					model.getAnimation().getKeyframes().get(keyframeLine.getSelected())
					.translations.remove(getPart(key.getKey()));
					posList.removeKey(key);
					pos = true;
					LogUtils.getLogger().info("deleted");
				}
			}
			if (rotKeys.length > 0) {
				for (Key key : rotKeys) {
					if(key == null) continue;
					JgModelPart part = getPart(key.getKey());
					if(part != null) {
						model.getAnimation().getKeyframes().get(keyframeLine.getSelected())
						.rotations.remove(part);
						rotList.removeKey(key);
						rot = true;
					}
				}
			}
			if (!pos && !rot) {
				if (model.getAnimation() != null && keyframeLine.getSelected() != -1) {
					Utils.updateKeyframesFromAnimation(model.getAnimation());
					keyframeLine.update(model.getAnimation());
				}
			}
		}));

		buttons.add(new Button(298, 59, 30, 20, new TranslatableComponent("Set"), (b) -> {
			if (model.getAnimation() != null && 
					!modelPartList.getSelectedIndexes().isEmpty() && 
					keyframeLine.getSelected() != -1) {
				Keyframe kf = getSelectedKeyframe();
				JgModelPart part = model.getPartsAsList().get(modelPartList
						.getSelectedIndexes().get(0));
				LogUtils.getLogger().info("sd");
				LogUtils.getLogger().info("index: " + modelPartList.getSelectedIndexes().get(0));
				if (!booleanCycles.get(0).getValue()) {
					LogUtils.getLogger().info("sd");
					if(!kf.translations.containsKey(part)) {
						kf.translations.put(part, new float[] { 0, 0, 0 });
						posList.addKey(new Key(font, part.getName()));
					}
					kf.translations.get(part)[0] = part.getTransform().pos[0];
					kf.translations.get(part)[1] = part.getTransform().pos[1];
					kf.translations.get(part)[2] = part.getTransform().pos[2];
				} else {
					if(!kf.rotations.containsKey(part)) {
						kf.rotations.put(part, new float[] { 0, 0, 0 });
						rotList.addKey(new Key(font, part.getName()));
					}
					kf.rotations.get(part)[0] = part.getTransform().rot[0];
					kf.rotations.get(part)[1] = part.getTransform().rot[1];
					kf.rotations.get(part)[2] = part.getTransform().rot[2];
				}
			}
			
		}));
		
		buttons.add(new Button(100, 144, 30, 20, new TranslatableComponent("Save"), (b) -> {
			if(model.getAnimation() != null) {
				AnimationSerializer.serializeWithCode(model.getAnimation(), model);
			}
		}));
		
		buttons.add(new Button(100, 122, 60, 20, new TranslatableComponent("UpdateParts"), (b) -> {
			model.getAnimator().switchUpdateParts();
			Utils.updateKeyframesFromAnimation(model.getAnimation());
			keyframeLine.update(model.getAnimation());
		}));
		
		buttons.add(new Button(132, 144, 28, 20, new TranslatableComponent("Quit"), (b) -> {
			if(model.getAnimation() != null) {
				model.getAnimator().finishAll();
			}
		}));
		
		buttons.add(new Button(164, 122, 30, 20, new TranslatableComponent("NextK"), (b) -> {
			if(model.getAnimation() != null) {
				if(current < model.getAnimation().getKeyframes().size()-1) {
					current++;
					Keyframe kf = model.getAnimation().getKeyframes().get(current);
					model.getAnimator().setTick(kf.startVisualTick);
					model.getAnimator().nextTick();
					LogUtils.getLogger().info("Current: " + current + " tick: " + 
							model.getAnimator().getTick());
					for(Entry<JgModelPart, float[]> e : kf.translations.entrySet()) {
						e.getKey().getTransform().pos[0] = Mth.lerp(1, 0, 
								e.getValue()[0]);
						e.getKey().getTransform().pos[1] = Mth.lerp(1, 0, 
								e.getValue()[1]);
						e.getKey().getTransform().pos[2] = Mth.lerp(1, 0, 
								e.getValue()[2]);
					}
					for(Entry<JgModelPart, float[]> e : kf.rotations.entrySet()) {
						e.getKey().getTransform().rot[0] = Mth.rotLerp(1, 0, 
								e.getValue()[0]);
						e.getKey().getTransform().rot[1] = Mth.rotLerp(1, 0, 
								e.getValue()[1]);
						e.getKey().getTransform().rot[2] = Mth.rotLerp(1, 0, 
								e.getValue()[2]);
					}
				} else {
					current = 0;
				}
			}
		}));
		
		buttons.add(new Button(164, 144, 30, 20, new TranslatableComponent("PrevK"), (b) -> {
			if(model.getAnimation() != null) {
				if(current > 0) {
					current--;
					Keyframe kf = model.getAnimation().getKeyframes().get(current);
					model.getAnimator().setTick(kf.startVisualTick);
					model.getAnimator().prevTick();
					LogUtils.getLogger().info("Current: " + current + " tick: " + 
							model.getAnimator().getTick());
					for(Entry<JgModelPart, float[]> e : kf.translations.entrySet()) {
						e.getKey().getTransform().pos[0] = Mth.lerp(1, 0, 
								e.getValue()[0]);
						e.getKey().getTransform().pos[1] = Mth.lerp(1, 0, 
								e.getValue()[1]);
						e.getKey().getTransform().pos[2] = Mth.lerp(1, 0, 
								e.getValue()[2]);
					}
					for(Entry<JgModelPart, float[]> e : kf.rotations.entrySet()) {
						e.getKey().getTransform().rot[0] = Mth.rotLerp(1, 0, 
								e.getValue()[0]);
						e.getKey().getTransform().rot[1] = Mth.rotLerp(1, 0, 
								e.getValue()[1]);
						e.getKey().getTransform().rot[2] = Mth.rotLerp(1, 0, 
								e.getValue()[2]);
					}
				}
			}
		}));
		
		buttons.add(new Button(164, 166, 60, 20, new TranslatableComponent("AddZeroKf"), (b) -> {
			Animation anim = model.getAnimation();
			if(anim != null) {
				if(keyframeLine.getSelected() == -1) {
					if(!anim.getKeyframes().isEmpty()) {
						Keyframe cero = new Keyframe(12);
						Keyframe last = anim.getKeyframes().get(anim.getKeyframes().size()-1);
						List<Integer> posIndexes = posList.getSelectedIndexes();
						if(!posIndexes.isEmpty()) {
							for(int i : posIndexes) {
								if(i >= posList.getKeys().size()) continue;
								cero.translations.put(Utils.getPartByName(model, 
										posList.getKeys().get(i).getKey()), 
										new float[] { 0, 0, 0 });
							}
						} else {
							for(Entry<JgModelPart, float[]> e : last.translations.entrySet()) {
								if(e.getValue()[0] != 0 || e.getValue()[1] != 0 || 
										e.getValue()[2] != 0) {
									cero.translations.put(e.getKey(), new float[] { 0, 0, 0 });
								}
								LogUtils.getLogger().info(Arrays.toString(e.getValue()));
							}
						}
						List<Integer> rotIndexes = rotList.getSelectedIndexes();
						if(!rotIndexes.isEmpty()) {
							for(int i : rotIndexes) {
								if(i >= rotList.getKeys().size()) continue;
								cero.rotations.put(Utils.getPartByName(model, 
										rotList.getKeys().get(i).getKey()), 
										new float[] { 0, 0, 0 });
							}
						} else {
							for(Entry<JgModelPart, float[]> e : last.rotations.entrySet()) {
								if(e.getValue()[0] != 0 || e.getValue()[1] != 0 || 
										e.getValue()[2] != 0) {
									cero.rotations.put(e.getKey(), new float[] { 0, 0, 0 });
								}
							}
						}
						anim.getKeyframes().add(cero);
						Utils.updateKeyframesFromAnimation(model.getAnimation());
						keyframeLine.update(model.getAnimation());
					} else {
						Keyframe cero = new Keyframe(12);
						for(JgModelPart part : model.getPartsAsList()) {
							cero.translations.put(part, new float[] { 0, 0, 0 });
							cero.rotations.put(part, new float[] { 0, 0, 0 });
						}
						anim.getKeyframes().add(cero);
						Utils.updateKeyframesFromAnimation(model.getAnimation());
						keyframeLine.update(model.getAnimation());
					}
				} else {
					Keyframe kf = getSelectedKeyframe();
					List<Integer> parts = modelPartList.getSelectedIndexes();
					if(!parts.isEmpty()) {
						for(int index : parts) {
							JgModelPart part = model.getPartsAsList().get(index);
							if (!booleanCycles.get(0).getValue()) {
								kf.translations.put(part, new float[] { 0, 0, 0 });
								posList.addKey(new Key(font, part.getName()));
							} else {
								kf.rotations.put(part, new float[] { 0, 0, 0 });
								rotList.addKey(new Key(font, part.getName()));
							}
						}
					}
				}
			}
		}));
		
		// Initializing widgets
		for (Button b : buttons) {
			addRenderableWidget(b);
		}

		for (EditBox e : edits) {
			addRenderableWidget(e);
		}

		for (OptionsList b : options) {
			addRenderableWidget(b);
		}
		for (CycleButton<Boolean> e : booleanCycles) {
			addRenderableWidget(e);
		}
		for (CycleButton<Integer> e : integerCycles) {
			addRenderableWidget(e);
		}

		if (model.getAnimation() != null) {
			keyframeLine.setAnimDur(model.getAnimation().getDuration());
			keyframeLine.setKeyframes(model.getAnimation().getKeyframes(), 
					model.getAnimation().getDuration());
		}
		init = true;
		// }
	}

	@Override
	public void render(PoseStack matrix, int x, int y, float p_96565_) {
		// Field Indicators
		this.font.drawShadow(matrix, "x: ", (float) 10 + (-AnimationScreen.this.font.width("x: ") / 2), (float) (4),
				16777215, true);
		this.font.drawShadow(matrix, "y: ", (float) 10 + (-AnimationScreen.this.font.width("y: ") / 2), (float) (24),
				16777215, true);
		this.font.drawShadow(matrix, "z: ", (float) 10 + (-AnimationScreen.this.font.width("z: ") / 2), (float) (44),
				16777215, true);
		this.font.drawShadow(matrix, "rx: ", (float) 10 + (-AnimationScreen.this.font.width("rx: ") / 2), (float) (64),
				16777215, true);
		this.font.drawShadow(matrix, "ry: ", (float) 10 + (-AnimationScreen.this.font.width("ry: ") / 2), (float) (84),
				16777215, true);
		this.font.drawShadow(matrix, "rz: ", (float) 10 + (-AnimationScreen.this.font.width("rz: ") / 2), (float) (104),
				16777215, true);
		this.font.drawShadow(matrix, "d: ", (float) 10 + (-AnimationScreen.this.font.width("d: ") / 2), (float) (124),
				16777215, true);
		this.font.drawShadow(matrix, "tms: ", (float) 10 + (-AnimationScreen.this.font.width("st: ") / 2), (float) (144),
				16777215, true);
		this.font.drawShadow(matrix, "s: ", (float) 10 + (-AnimationScreen.this.font.width("st: ") / 2), (float) (164),
				16777215, true);
		this.font.drawShadow(matrix, "es: ", (float) 10 + (-AnimationScreen.this.font.width("es: ") / 2), (float) (184),
				16777215, true);
		if(model.getAnimation() != null) {
			this.font.drawShadow(matrix, "Animation Name: " + model.getAnimation().getName(),
					(float) 180,
					(float) (192), 16777215, true);
		}
		this.font.drawShadow(matrix, "Update Parts: " + model.getAnimator().shouldUpdateParts(),
				(float) 14
						+ (-AnimationScreen.this.font.width("Update Parts: " + model.getAnimator().shouldUpdateParts()) / 2),
				(float) (208), 16777215, true);
		this.font.drawShadow(matrix, "Current: " + current,
				(float) 28
						+ (-AnimationScreen.this.font.width("Current: " + current) / 2),
				(float) (222), 16777215, true);
		
		renderWidget(matrix, 100, 14, 0, 66, 200, 20, 100, 20);
		renderWidget(matrix, 100, 51, 0, 46, 200, 20, 100, -14);

		super.render(matrix, x, y, p_96565_);

		// Gun Parts Selection Rendering
		modelPartList.render(matrix, x, y, p_96565_);
		posList.render(matrix, x, y, p_96565_);
		rotList.render(matrix, x, y, p_96565_);

		keyframeLine.render(matrix, x, y, p_96565_);
		minX = 100;
		maxX = 400;
		deltaX = maxX - minX;
	}

	@Override
	public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
		keyframeLine.onClick((int) p_94695_, (int) p_94696_);
		posList.onClick((int) p_94695_, (int) p_94696_, ctrl);
		rotList.onClick((int) p_94695_, (int) p_94696_, ctrl);
		modelPartList.onClick((int) p_94695_, (int) p_94696_, ctrl);
		return super.mouseClicked(p_94695_, p_94696_, p_94697_);
	}

	@Override
	public void tick() {
		super.tick();
		this.posList.tick();
		this.rotList.tick();
		modelPartList.tick();
	}

	public Keyframe getSelectedKeyframe() {
		return model.getAnimator().getAnimation().getKeyframes().get(
				keyframeLine.getSelected() > 
				model.getAnimator().getAnimation().getKeyframes().size()-1 ? 
					model.getAnimator().getAnimation().getKeyframes().size()-1 : 
					keyframeLine.getSelected());
	}
	
	@Override
	public boolean mouseDragged(double p_94699_, double p_94700_, int p_94701_, double p_94702_, double p_94703_) {
		posList.check((int) p_94699_, (int) p_94700_);
		rotList.check((int) p_94699_, (int) p_94700_);
		modelPartList.check((int) p_94699_, (int) p_94700_);
		keyframeLine.check((int) p_94699_, (int) p_94700_);
		return super.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
	}

	@Override
	public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
		return super.mouseReleased(p_94722_, p_94723_, p_94724_);
	}

	@Override
	public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
		posList.onScroll((float) (p_94686_ * (-p_94688_)));
		rotList.onScroll((float) (p_94686_ * (-p_94688_)));
		modelPartList.onScroll((float) (p_94686_ * (-p_94688_)));
		return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
	}

	@Override
	public boolean keyPressed(int key, int p_96553_, int p_96554_) {
		keys[key] = true;
		System.out.println(key);
		if (model.getAnimation() != null) {
			for(EditBox edit : edits) {
				if(edit.isFocused()) {
					return super.keyPressed(key, p_96553_, p_96554_);
				}
			}
			if (key == 129) {
				ctrl = true;
			}
			try {
			// Ctrl + c
			if (keys[341] && keys[67]) {
				if (keyframeLine.getSelected() != -1) {
					if (booleanCycles.get(0).getValue()) {
						kf = getSelectedKeyframe();
					} else {
						transforms.clear();
						if (booleanCycles.get(0).getValue()) {
							List<Integer> indices = posList.getSelectedIndexes();
							for (int i : indices) {
								JgModelPart part = getPart(posList.getKeys().get(i).getKey());
								transforms.add(new Pair<JgModelPart, float[]>(part,
										getSelectedKeyframe().translations.get(part).clone()));
							}
						} else {
							List<Integer> indices = rotList.getSelectedIndexes();
							for (int i : indices) {
								JgModelPart part = getPart(rotList.getKeys().get(i).getKey());
								transforms.add(new Pair<JgModelPart, float[]>(part,
										getSelectedKeyframe().rotations.get(part).clone()));
							}
						}
					}
				}
			}
			// Ctrl + v
			if (keys[341] && keys[86]) {
				if (model.getAnimation() != null) {
					System.out.println("here6");
					if (keyframeLine.getSelected() != -1) {
						System.out.println("here5");
						if (booleanCycles.get(0).getValue()) {
							System.out.println("here4");
							if (kf != null) {
								model.getAnimation().getKeyframes()
									.get(keyframeLine.getSelected())
									.copyTransformFrom(kf);
							}
						} else {
							System.out.println("here3");
							if (!transforms.isEmpty()) {
								Keyframe kf = getSelectedKeyframe();
								System.out.println("here2");
								for (Pair<JgModelPart, float[]> pair : transforms) {
									if (booleanCycles.get(0).getValue()) {
										kf.translations.put(pair.getLeft(), pair.getRight());
										Key[] newPosKeys = new Key[kf.translations.size()];
										int j = 0;
										for (JgModelPart s : kf.translations.keySet()) {
											newPosKeys[j] = new Key(font, s.getName());
											j++;
										}
										posList.setKeys(newPosKeys);
										System.out.println("here1");
									} else {
										kf.rotations.put(pair.getLeft(), pair.getRight());
										Key[] newRotKeys = new Key[kf.rotations.size()];
										int j = 0;
										for (JgModelPart s : kf.rotations.keySet()) {
											newRotKeys[j] = new Key(font, s.getName());
											j++;
										}
										rotList.setKeys(newRotKeys);
										System.out.println("here0");
									}
								}
							}
						}
					}
				}
				System.out.println("Pressing");
			}
	
			// Ctrl + d
			if (keys[341] && keys[68]) {
				if (keyframeLine.getSelected() != -1) {
					Keyframe kfselCopy = getSelectedKeyframe().copy();
					Utils.insertInto(keyframeLine.getSelected(), 
							model.getAnimation().getKeyframes(), kfselCopy);
					Utils.updateKeyframesFromAnimation(model.getAnimation());
					keyframeLine.update(model.getAnimation());
				}
			}
			
			// Suprimir
			if(keys[261]) {
				if(keyframeLine.getSelected() != -1) {
					model.getAnimation().getKeyframes().remove(keyframeLine.getSelected());
					Utils.updateKeyframesFromAnimation(model.getAnimation());
					keyframeLine.update(model.getAnimation());
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(keys[341] && keys[162]) {
			for(JgModelPart part : model.getPartsAsList()) {
				LogUtils.getLogger().info("Name: " + part.getName() + " dtransform: "
						+ part.getDTransform().toString());
			}
		}
		return super.keyPressed(key, p_96553_, p_96554_);
	}

	@Override
	public boolean keyReleased(int p_94715_, int p_94716_, int p_94717_) {
		keys[p_94715_] = false;
		if (p_94715_ == 341) {
			ctrl = false;
		}
		return super.keyReleased(p_94715_, p_94716_, p_94717_);
	}
	
	public JgModelPart getPart(String key) {
		for (JgModelPart p : model.getPartsAsList()) {
			if (p.getName().equals(key)) {
				return p;
			}
		}
		return null;
	}

	public Transform getTransform() {
		Key key = modelPartList.getSelectedKey();
		if (key != null) {
			return ((JgPartKey) modelPartList.getSelectedKey()).getPart().getTransform();
		}
		return Transform.EMPTY;
	}

	public void setVecVal(boolean rot, int i, float v) {
		Keyframe kf = model.getAnimation().getKeyframes().get(keyframeLine.getSelected());
		if (!rot) {
			LogUtils.getLogger().info("SA");
			if (!kf.translations.isEmpty()) {
				LogUtils.getLogger().info("Not Empty");
				Key[] keys = posList.getSelectedKeys();
				for (Key key : keys) {
					kf.translations.get(Utils.getPartByName(model, key.getKey()))[i] = v;
					LogUtils.getLogger().info("key: " + key.getKey());
				}
			}
		} else {
			if (!kf.rotations.isEmpty()) {
				Key[] keys = rotList.getSelectedKeys();
				for (Key key : keys) {
					kf.rotations.get(Utils.getPartByName(model, key.getKey()))[i] = v;
				}
			}
		}
	}

	public void renderWidget(PoseStack matrix, int x, int y, int i, int j, int w, int h, int w2, int h2) {
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, WIDGETS);
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		bufferbuilder.vertex((i) + x, ((j + h) + y) + h2, 0).uv(0, (j + h) / 256f).color(1.0F, 1.0F, 1.0F, 1.0f)
				.endVertex();
		bufferbuilder.vertex(((i + w) + x) + w2, ((j + h) + y) + h2, 0).uv((i + w) / 256f, (j + h) / 256f)
				.color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
		bufferbuilder.vertex(((i + w) + x) + w2, (j) + y, 0).uv((i + w) / 256f, (j) / 256f)
				.color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
		bufferbuilder.vertex((i) + x, (j) + y, 0).uv(0, (j) / 256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
		tesselator.end();
	}

	public void initKeyframes() {
		if (model.getAnimation() != null) {
			keyframeLine.setAnimDur(model.getAnimation().getDuration());
			keyframeLine.setKeyframes(model.getAnimation().getKeyframes(), 
					model.getAnimation().getDuration());
		}
	}

	public Font getFont() {
		return font;
	}

	public JgModel getModel() {
		return model;
	}
	
	public int getCurrent() {
		return current;
	}

	public KeyframeLineWidget getKeyframeLineWidget() {
		return keyframeLine;
	}

	public JGSelectionList getJgPartList() {
		return modelPartList;
	}

	public JGSelectionList getPosList() {
		return posList;
	}

	public JGSelectionList getRotList() {
		return rotList;
	}

	public List<EditBox> getEditBoxes() {
		return edits;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public List<CycleButton<Integer>> getIntCycles() {
		return integerCycles;
	}

	public List<CycleButton<Boolean>> getBooleanCycles() {
		return booleanCycles;
	}

	private CycleButton<Integer> buildIntCycle(Function<Integer, Component> f, int initVal, 
			int x, int y, int w, int h,
			Component t, OnValueChange<Integer> ch, Integer... values) {
		CycleButton<Integer> cycle = CycleButton.builder(f).withValues(values).withInitialValue(initVal).create(x, y, w,
				h, t, ch);
		return cycle;
	}

	private CycleButton<Boolean> buildBooleanCycle(Function<Boolean, Component> f, 
			boolean initVal, int x, int y, int w,
			int h, Component t, OnValueChange<Boolean> ch) {
		CycleButton<Boolean> cycle = CycleButton.builder(f).withValues(true, false).withInitialValue(initVal).create(x,
				y, w, h, t, ch);
		return cycle;
	}
	
	public static class Pair<T, R> {
		protected T t;
		protected R r;

		public Pair(T t, R r) {
			this.t = t;
			this.r = r;
		}

		public T getLeft() {
			return t;
		}

		public R getRight() {
			return r;
		}

	}

}
