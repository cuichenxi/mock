/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qunar.mk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* ListDemo.java requires no other files. */
public class MK extends JFrame implements ListSelectionListener {
	private JList<String> list;
	private static final String addString = "增加";
	private static final String refreshString = "刷新";
	private static final String deleString = "删除";
	private static final String updateString = "修改保存";
	private static final String startString = "启动服务";
	private static final String stopString = "停止服务";
	private JButton deleButton;
	private JButton updateButton;
	private JTextField nameTextField;
	private JButton ipLable;
	private AbstractButton startButton;
	private JButton stopButton;
	private ArrayList<File> data;
	private DefaultListModel<String> listModel;
	private MKJsonTextField jsonText;
	private AbstractButton refreshButton;
	private String[] dataNames;
	public File file;
	public int temIndex = 0;
	private JButton addButton;

	public MK(String string) {
		super(string);
		addButton = new JButton(addString);
		addButton.setActionCommand(addString);

		deleButton = new JButton(deleString);
		deleButton.setActionCommand(deleString);
		deleButton.addActionListener(new deleListener());

		refreshButton = new JButton(refreshString);
		refreshButton.setActionCommand(refreshString);

		startButton = new JButton(startString);
		startButton.setActionCommand(startString);

		stopButton = new JButton(stopString);
		stopButton.setActionCommand(stopString);

		ipLable = new JButton("获取ip中...");

		// Create a panel that uses BoxLayout.
		nameTextField = new JTextField(20);

		updateButton = new JButton(updateString);
		updateButton.setActionCommand(updateString);

		JPanel headPanel = new JPanel();
		headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.LINE_AXIS));
		headPanel.add(nameTextField);
		headPanel.add(Box.createHorizontalStrut(5));
		headPanel.add(updateButton);
		headPanel.add(Box.createHorizontalStrut(5));
		headPanel.add(deleButton);
		headPanel.add(Box.createHorizontalStrut(5));
		headPanel.add(addButton);
		headPanel.add(Box.createHorizontalStrut(5));
		headPanel.add(refreshButton);
		headPanel.add(Box.createHorizontalStrut(5));
		headPanel.add(ipLable);
		headPanel.add(Box.createHorizontalStrut(5));
		headPanel.add(startButton);
		headPanel.add(Box.createHorizontalStrut(5));
		headPanel.add(stopButton);
		headPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(headPanel, BorderLayout.PAGE_START);

		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(100);
		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setPreferredSize(new Dimension(150, 500));
		JPanel centerFrame = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		centerFrame.setLayout(layout);
		add(centerFrame);
		jsonText = new MKJsonTextField();
		centerFrame.add(listScrollPane);
		centerFrame.add(jsonText.getContentPane());
		GridBagConstraints s = new GridBagConstraints();
		s.fill = GridBagConstraints.BOTH;
		s.gridwidth = 1;
		s.weightx = 0.2;
		s.weighty = 1;
		layout.setConstraints(listScrollPane, s);// 设置组件
		s.gridwidth = 0;
		s.weightx = 1;
		s.weighty = 1;
		layout.setConstraints(jsonText.getContentPane(), s);// 设置组件
		centerFrame.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		centerFrame.setBorder(BorderFactory.createTitledBorder("数据列表"));
		setData();
	}

	void setData() {
		initData();
		refreshListData();
		boolean isStart = MKUitls.startServer();
		startButton.setEnabled(!isStart);
		stopButton.setEnabled(isStart);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isStart = MKUitls.startServer();
				startButton.setEnabled(!isStart);
				stopButton.setEnabled(isStart);
			}
		});
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isStop = MKUitls.stopServer();
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
			}
		});

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String str = sdf.format(date);
				String fileName = str + ".json";
				MKUitls.createFile(fileName);
				refreshListData();
			}
		});
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				refreshListData();
			}

		});
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (file != null && file.exists()) {
					String fileName = nameTextField.getText().toString().trim();
					String fileContent = jsonText.textArea.getText().toString().trim();
					if (fileName == null || fileName.length() == 0) {
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null, "请输入文件名", "提示", JOptionPane.ERROR_MESSAGE);
						// int response = JOptionPane.showConfirmDialog(null,
						// "请输入文件名", "提示",
						// JOptionPane.WARNING_MESSAGE);
						refreshListData();
						return;
					}
					if (fileContent == null || fileContent.length() == 0) {
						JOptionPane.showMessageDialog(null, "内容不能为空", "提示", JOptionPane.ERROR_MESSAGE);
						refreshListData();
						return;
					}
					MKUitls.copyFile(file, fileName, fileContent);
					refreshListData();
				}
			}
		});
		ipLable.setText(MKUitls.getIP());
		ipLable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ipLable.setText(MKUitls.getIP());
			}
		});
		jsonText.jbtnformat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				file = data.get(temIndex);
				try {
					String fileString = MKUitls.loadAFileToStringDE2(file);
//					fileString =FormatJson.formatJson(fileString);
					jsonText.textArea.setText(fileString);
					nameTextField.setText(file.getName());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		this.addWindowListener(new MWindowsListener());
	}

	private void initData() {
		MKUitls.initData();
		File f2 = new File(this.getClass().getResource("").getPath());
		System.out.println(f2);	
	}

	private void refreshListData() {
		data = MockData.getData();
		listModel = MockData.getDataModle(data);
		dataNames = MockData.getDataNames(listModel);
		list.removeAll();
		if(dataNames!=null){
			list.setListData(dataNames);
		}
		if (dataNames != null && dataNames.length > temIndex) {
			list.setSelectedIndex(temIndex);
		}
	}

	class deleListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// This method can be called only if
			// there's a valid selection
			// so go ahead and remove whatever's selected.
			int index = list.getSelectedIndex();
			listModel.remove(index);

			int size = listModel.getSize();

			if (size == 0) { // Nobody's left, disable firing.
				deleButton.setEnabled(false);

			} else { // Select an index.
				File file = data.get(index);

				int response = JOptionPane.showConfirmDialog(null, "你确定删除" + file.getName() + "?", "提示",
						JOptionPane.YES_NO_OPTION);
				if (response == 0) {
					boolean isDele = MKUitls.deleFile(file);
					if (isDele) {
						if (index == listModel.getSize()) {
							// removed item in last position
							index--;
						}
						list.ensureIndexIsVisible(index);
						refreshListData();
					}
				} else if (response == 1) {

				}

			}
		}
	}

	// This method is required by ListSelectionListener.
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			int index = list.getSelectedIndex();
			if (index == -1) {
				// No selection, disable dele button.
				deleButton.setEnabled(false);

			} else {
				temIndex = index;
				// Selection, enable the dele button.
				deleButton.setEnabled(true);
				file = data.get(index);
				try {
					String fileString = MKUitls.loadAFileToStringDE2(file);
//					fileString =FormatJson.formatJson(fileString);
					jsonText.textArea.setText(fileString);
					nameTextField.setText(file.getName());
					jsonText.jScrollPane1.getViewport().setViewPosition(new Point(0, 0));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
	}

	class MWindowsListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			boolean isStop = MKUitls.stopServer();
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		MK frame = new MK("MK-JSON");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 800, 775);// 窗口居中

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
