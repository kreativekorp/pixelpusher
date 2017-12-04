package com.kreative.pixelpusher.animation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.kreative.pixelpusher.array.PixelArrayEditor;

public class AnimationEditor extends PixelArrayEditor<AnimationArray> {
	private static final long serialVersionUID = 1L;
	
	public AnimationEditor() {
		this(new AnimationArray());
	}
	
	public AnimationEditor(AnimationArray array) {
		super(array);
		setLayout(new BorderLayout());
		add(makeMainPanel(), BorderLayout.CENTER);
	}
	
	@Override
	public void setPixelSet(AnimationArray array) {
		super.setPixelSet(array);
		removeAll();
		add(makeMainPanel(), BorderLayout.CENTER);
		revalidate();
	}
	
	private JTextField animationPath;
	private JButton animationBrowse;
	private JRadioButton hscrollNone;
	private JRadioButton hscrollLeft;
	private JRadioButton hscrollRight;
	private JCheckBox hwrap;
	private SpinnerNumberModel hmsModel;
	private JSpinner hmsSpinner;
	private JRadioButton vscrollNone;
	private JRadioButton vscrollUp;
	private JRadioButton vscrollDown;
	private JCheckBox vwrap;
	private SpinnerNumberModel vmsModel;
	private JSpinner vmsSpinner;
	
	private JPanel makeMainPanel() {
		JLabel animationLabel = new JLabel("Animation:");
		animationPath = new JTextField();
		animationPath.setEditable(false);
		new DropTarget(animationPath, new MyDropTarget());
		animationBrowse = new JButton("Browse...");
		new DropTarget(animationBrowse, new MyDropTarget());
		JLabel hscrollLabel = new JLabel("Horizontal:");
		hscrollNone = new JRadioButton("Static");
		hscrollNone.setSelected(pixelSet.getScrollDirectionHorizontal() == 0);
		hscrollLeft = new JRadioButton("Scroll Left");
		hscrollLeft.setSelected(pixelSet.getScrollDirectionHorizontal() < 0);
		hscrollRight = new JRadioButton("Scroll Right");
		hscrollRight.setSelected(pixelSet.getScrollDirectionHorizontal() > 0);
		ButtonGroup hscrollGroup = new ButtonGroup();
		hscrollGroup.add(hscrollNone);
		hscrollGroup.add(hscrollLeft);
		hscrollGroup.add(hscrollRight);
		hwrap = new JCheckBox("Wrap");
		hwrap.setSelected(pixelSet.getWrapHorizontal());
		JLabel hmsLabel1 = new JLabel("Speed:");
		JLabel hmsLabel2 = new JLabel("ms");
		hmsModel = new SpinnerNumberModel(pixelSet.getScrollMsPerFrameHorizontal(), 20, 2000, 10);
		hmsSpinner = new JSpinner(hmsModel);
		JLabel vscrollLabel = new JLabel("Vertical:");
		vscrollNone = new JRadioButton("Static");
		vscrollNone.setSelected(pixelSet.getScrollDirectionVertical() == 0);
		vscrollUp = new JRadioButton("Scroll Up");
		vscrollUp.setSelected(pixelSet.getScrollDirectionVertical() < 0);
		vscrollDown = new JRadioButton("Scroll Down");
		vscrollDown.setSelected(pixelSet.getScrollDirectionVertical() > 0);
		ButtonGroup vscrollGroup = new ButtonGroup();
		vscrollGroup.add(vscrollNone);
		vscrollGroup.add(vscrollUp);
		vscrollGroup.add(vscrollDown);
		vwrap = new JCheckBox("Wrap");
		vwrap.setSelected(pixelSet.getWrapVertical());
		JLabel vmsLabel1 = new JLabel("Speed:");
		JLabel vmsLabel2 = new JLabel("ms");
		vmsModel = new SpinnerNumberModel(pixelSet.getScrollMsPerFrameVertical(), 20, 2000, 10);
		vmsSpinner = new JSpinner(vmsModel);
		
		JPanel animationPanel = new JPanel(new BorderLayout(4,4));
		animationPanel.add(animationLabel, BorderLayout.LINE_START);
		animationPanel.add(animationPath, BorderLayout.CENTER);
		animationPanel.add(animationBrowse, BorderLayout.LINE_END);
		JPanel hscrollPanel = new JPanel(new GridLayout(0,1));
		hscrollPanel.add(hscrollNone);
		hscrollPanel.add(hscrollLeft);
		hscrollPanel.add(hscrollRight);
		JPanel hscrollMsPanel = new JPanel(new BorderLayout(4,4));
		hscrollMsPanel.add(hmsLabel1, BorderLayout.LINE_START);
		hscrollMsPanel.add(hmsSpinner, BorderLayout.CENTER);
		hscrollMsPanel.add(hmsLabel2, BorderLayout.LINE_END);
		final CardLayout hscrollMiscLayout = new CardLayout();
		final JPanel hscrollMiscPanel = new JPanel(hscrollMiscLayout);
		hscrollMiscPanel.add(hwrap, "static");
		hscrollMiscPanel.add(hscrollMsPanel, "scroll");
		hscrollMiscLayout.show(hscrollMiscPanel, (pixelSet.getScrollDirectionHorizontal() == 0) ? "static" : "scroll");
		JPanel hPanel = new JPanel(new BorderLayout(4,4));
		hPanel.add(hscrollLabel, BorderLayout.PAGE_START);
		hPanel.add(hscrollPanel, BorderLayout.CENTER);
		hPanel.add(hscrollMiscPanel, BorderLayout.PAGE_END);
		JPanel vscrollPanel = new JPanel(new GridLayout(0,1));
		vscrollPanel.add(vscrollNone);
		vscrollPanel.add(vscrollUp);
		vscrollPanel.add(vscrollDown);
		JPanel vscrollMsPanel = new JPanel(new BorderLayout(4,4));
		vscrollMsPanel.add(vmsLabel1, BorderLayout.LINE_START);
		vscrollMsPanel.add(vmsSpinner, BorderLayout.CENTER);
		vscrollMsPanel.add(vmsLabel2, BorderLayout.LINE_END);
		final CardLayout vscrollMiscLayout = new CardLayout();
		final JPanel vscrollMiscPanel = new JPanel(vscrollMiscLayout);
		vscrollMiscPanel.add(vwrap, "static");
		vscrollMiscPanel.add(vscrollMsPanel, "scroll");
		vscrollMiscLayout.show(vscrollMiscPanel, (pixelSet.getScrollDirectionVertical() == 0) ? "static" : "scroll");
		JPanel vPanel = new JPanel(new BorderLayout(4,4));
		vPanel.add(vscrollLabel, BorderLayout.PAGE_START);
		vPanel.add(vscrollPanel, BorderLayout.CENTER);
		vPanel.add(vscrollMiscPanel, BorderLayout.PAGE_END);
		JPanel hvPanel = new JPanel(new GridLayout(1,0,8,8));
		hvPanel.add(hPanel);
		hvPanel.add(vPanel);
		JPanel mainPanel = new JPanel(new BorderLayout(8,8));
		mainPanel.add(animationPanel, BorderLayout.PAGE_START);
		mainPanel.add(hvPanel, BorderLayout.CENTER);
		
		animationBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame owner = getOwner();
				FileDialog fd = new FileDialog(owner, "Load Animation", FileDialog.LOAD);
				fd.setVisible(true);
				if (fd.getDirectory() != null && fd.getFile() != null) {
					File f = new File(fd.getDirectory(), fd.getFile());
					setFile(owner, f);
				}
			}
		});
		hscrollNone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.setScrollDirectionHorizontal(0);
				hscrollMiscLayout.show(hscrollMiscPanel, "static");
			}
		});
		hscrollLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.setScrollDirectionHorizontal(-1);
				hscrollMiscLayout.show(hscrollMiscPanel, "scroll");
			}
		});
		hscrollRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.setScrollDirectionHorizontal(1);
				hscrollMiscLayout.show(hscrollMiscPanel, "scroll");
			}
		});
		hwrap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.setWrapHorizontal(hwrap.isSelected());
			}
		});
		hmsModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setScrollMsPerFrameHorizontal(hmsModel.getNumber().intValue());
			}
		});
		vscrollNone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.setScrollDirectionVertical(0);
				vscrollMiscLayout.show(vscrollMiscPanel, "static");
			}
		});
		vscrollUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.setScrollDirectionVertical(-1);
				vscrollMiscLayout.show(vscrollMiscPanel, "scroll");
			}
		});
		vscrollDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.setScrollDirectionVertical(1);
				vscrollMiscLayout.show(vscrollMiscPanel, "scroll");
			}
		});
		vwrap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pixelSet.setWrapVertical(vwrap.isSelected());
			}
		});
		vmsModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pixelSet.setScrollMsPerFrameVertical(vmsModel.getNumber().intValue());
			}
		});
		
		return mainPanel;
	}
	
	private Frame getOwner() {
		Container c = this.getParent();
		while (true) {
			if (c == null) return null;
			else if (c instanceof Frame) return (Frame)c;
			else c = c.getParent();
		}
	}
	
	private void setFile(Frame owner, File f) {
		try {
			pixelSet.setAnimation(f);
			animationPath.setText(f.getAbsolutePath());
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(owner, "Could not read file.", "Load Animation", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class MyDropTarget implements DropTargetListener {
		@Override
		public void drop(DropTargetDropEvent dtde) {
			try {
				int action = dtde.getDropAction();
				dtde.acceptDrop(action);
				Transferable t = dtde.getTransferable();
				if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					List<?> list = (List<?>)t.getTransferData(DataFlavor.javaFileListFlavor);
					for (Object o : list) {
						if (o instanceof File) {
							setFile(getOwner(), (File)o);
						}
					}
					dtde.dropComplete(true);
				} else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String s = t.getTransferData(DataFlavor.stringFlavor).toString();
					for (String l : s.split("\r\n|\r|\n")) {
						l = l.trim();
						if (l.length() > 0) {
							setFile(getOwner(), new File(l));
						}
					}
					dtde.dropComplete(true);
				} else {
					dtde.dropComplete(false);
				}
			} catch (Exception e) {
				dtde.dropComplete(false);
			}
		}
		@Override public void dragEnter(DropTargetDragEvent dtde) {}
		@Override public void dragExit(DropTargetEvent dte) {}
		@Override public void dragOver(DropTargetDragEvent dtde) {}
		@Override public void dropActionChanged(DropTargetDragEvent dtde) {}
	}
}
