/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2010 psiinon@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */

package org.zaproxy.zap.view;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.control.Control.Mode;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.view.View;

public class MainToolbarPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(MainToolbarPanel.class);
	
	private JToolBar toolbar = null;
 	private JComboBox<String> modeSelect = null;
	private JButton btnNew = null;
	private JButton btnOpen = null;
	private JButton btnSave = null;
	private JButton btnSnapshot = null;
	private JButton btnSession = null;
	private JButton btnOptions = null;

	private JToggleButton btnExpandSites = null;
	private JToggleButton btnExpandReports = null;

	public MainToolbarPanel () {
		super();
		initialise();
	}
	
	public void initialise () {
		setLayout(new java.awt.GridBagLayout());
		setPreferredSize(new java.awt.Dimension(80000,25));
		setMaximumSize(new java.awt.Dimension(80000,25));
		this.setBorder(BorderFactory.createEtchedBorder());

		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;

		JToolBar t1 = new JToolBar();
		t1.setEnabled(true);
		t1.setPreferredSize(new java.awt.Dimension(80000,25));
		t1.setMaximumSize(new java.awt.Dimension(80000,25));
		
		add(getToolbar(), gridBagConstraints1);
		add(t1, gridBagConstraints2);

		toolbar.add(getModeSelect());
		toolbar.add(getBtnNew());
		toolbar.add(getBtnOpen());
		toolbar.add(getBtnSave());
		toolbar.add(getBtnSnapshot());
		toolbar.add(getBtnSession());
		toolbar.add(getBtnOptions());
		
		toolbar.addSeparator();

		toolbar.add(getBtnExpandSites());
		toolbar.add(getBtnExpandReports());
		
		toolbar.addSeparator();

	}
	
	private JToolBar getToolbar() {
		if (toolbar == null) {
			toolbar = new JToolBar();
			toolbar.setEnabled(true);
			toolbar.setFloatable(false);
			toolbar.setRollover(true);
			toolbar.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			toolbar.setName("Main Toolbar");
			toolbar.setBorder(BorderFactory.createEmptyBorder());
		}
		return toolbar;
	}
	
	public void addButton (JButton button) {
		getToolbar().add(button);
	}

	public void removeButton(JButton button) {
		getToolbar().remove(button);
	}

	public void addButton(JToggleButton button) {
		getToolbar().add(button);
	}

	public void removeButton(JToggleButton button) {
		getToolbar().remove(button);
	}

	public void addSeparator() {
		getToolbar().addSeparator();
	}

	public void addSeparator(JToolBar.Separator separator) {
		getToolbar().add(separator);
	}

	public void removeSeparator(JToolBar.Separator separator) {
		getToolbar().remove(separator);
	}
	
	private JComboBox<String> getModeSelect() {
		if (modeSelect == null) {
			modeSelect = new JComboBox<>();
			modeSelect.addItem(Constant.messages.getString("view.toolbar.mode.safe.select"));
			modeSelect.addItem(Constant.messages.getString("view.toolbar.mode.protect.select"));
			modeSelect.addItem(Constant.messages.getString("view.toolbar.mode.standard.select"));
			
			modeSelect.setToolTipText(Constant.messages.getString("view.toolbar.mode.tooltip"));
			// Increase the time the tooltip is displayed, to give people a chance to read it!
			ToolTipManager.sharedInstance().setDismissDelay(12000);
			ToolTipManager.sharedInstance().registerComponent(modeSelect);
			
			// Control wont have finished initialising yet, so get from the params
			Mode mode = Mode.valueOf(Model.getSingleton().getOptionsParam().getViewParam().getMode());
			switch (mode) {
				case safe:		modeSelect.setSelectedIndex(0); break;
				case protect:	modeSelect.setSelectedIndex(1); break;
				case standard:	modeSelect.setSelectedIndex(2); break;
			}

			modeSelect.addActionListener(new java.awt.event.ActionListener() { 
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Mode mode = null;
					switch(modeSelect.getSelectedIndex()) {
						case 0:	mode = Mode.safe;	break;
						case 1:	mode = Mode.protect;	break;
						case 2:	mode = Mode.standard;	break;
						default: return;	// Not recognised
					}
					Control.getSingleton().setMode(mode);
				}
			});
		}
		return modeSelect;
	}

	private JButton getBtnNew() {
		if (btnNew == null) {
			btnNew = new JButton();
			btnNew.setIcon(new ImageIcon(MainToolbarPanel.class.getResource("/resource/icon/16/021.png")));	// 'Blank file' icon
			btnNew.setToolTipText(Constant.messages.getString("menu.file.newSession"));

			btnNew.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						Control.getSingleton().getMenuFileControl().newSession(true);
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						View.getSingleton().showWarningDialog(Constant.messages.getString("menu.file.newSession.error"));
					}
				}
			});
		}
		return btnNew;
	}

	private JButton getBtnOpen() {
		if (btnOpen == null) {
			btnOpen = new JButton();
			btnOpen.setIcon(new ImageIcon(MainToolbarPanel.class.getResource("/resource/icon/16/047.png")));	// 'open folder' icon
			btnOpen.setToolTipText(Constant.messages.getString("menu.file.openSession"));

			btnOpen.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						Control.getSingleton().getMenuFileControl().openSession();
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						View.getSingleton().showWarningDialog(Constant.messages.getString("menu.file.openSession.error"));
					}
				}
			});
		}
		return btnOpen;
	}

	private JButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JButton();
			btnSave.setIcon(new ImageIcon(MainToolbarPanel.class.getResource("/resource/icon/16/096.png")));	// 'diskette' icon
			btnSave.setToolTipText(Constant.messages.getString("menu.file.persistSession"));

			btnSave.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						if (Model.getSingleton().getSession().isNewState()) {
							Control.getSingleton().getMenuFileControl().saveAsSession();
						} else {
							View.getSingleton().showWarningDialog(Constant.messages.getString("menu.file.sessionExists.error"));
						}
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						View.getSingleton().showWarningDialog(Constant.messages.getString("menu.file.persistSession.error"));
					}
				}
			});
		}
		return btnSave;
	}

	private JButton getBtnSnapshot() {
		if (btnSnapshot == null) {
			btnSnapshot = new JButton();
			btnSnapshot.setIcon(new ImageIcon(MainToolbarPanel.class.getResource("/resource/icon/fugue/camera.png")));
			btnSnapshot.setToolTipText(Constant.messages.getString("menu.file.snapshotSession"));

			btnSnapshot.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						if (Model.getSingleton().getSession().isNewState()) {
							View.getSingleton().showWarningDialog(Constant.messages.getString("menu.file.sessionNotExist.error"));
						} else {
							Control.getSingleton().getMenuFileControl().saveSnapshot();
						}
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						View.getSingleton().showWarningDialog(Constant.messages.getString("menu.file.persistSession.error"));
					}
				}
			});
		}
		return btnSnapshot;
	}

	private JButton getBtnSession() {
		if (btnSession == null) {
			btnSession = new JButton();
			btnSession.setIcon(new ImageIcon(MainToolbarPanel.class.getResource("/resource/icon/16/024.png")));	// 'spreadsheet' icon
			btnSession.setToolTipText(Constant.messages.getString("menu.file.sessionProperties"));

			btnSession.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						View.getSingleton().showSessionDialog(Model.getSingleton().getSession(), null);
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						View.getSingleton().showWarningDialog(Constant.messages.getString("menu.file.SessionSession.error"));
					}
				}
			});
		}
		return btnSession;
	}

	private JButton getBtnOptions() {
		if (btnOptions == null) {
			btnOptions = new JButton();
			btnOptions.setToolTipText(Constant.messages.getString("menu.tools.options"));
			btnOptions.setIcon(new ImageIcon(MainToolbarPanel.class.getResource("/resource/icon/16/041.png")));
			btnOptions.addActionListener(new ActionListener () {
				@Override
				public void actionPerformed(ActionEvent e) {
					Control.getSingleton().getMenuToolsControl().options();
				}
			});
		}
		return btnOptions;
	}

	private JToggleButton getBtnExpandSites() {
		if (btnExpandSites == null) {
			btnExpandSites = new JToggleButton();
			btnExpandSites.setIcon(new ImageIcon(MainToolbarPanel.class.getResource("/resource/icon/expand_sites.png")));
			btnExpandSites.setToolTipText(Constant.messages.getString("view.toolbar.expandSites"));
			
			btnExpandSites.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (Model.getSingleton().getOptionsParam().getViewParam().getDisplayOption() != View.DISPLAY_OPTION_LEFT_FULL) {
						getBtnExpandReports().setSelected(false);
						
						View.getSingleton().getMainFrame().changeDisplayOption(View.DISPLAY_OPTION_LEFT_FULL);
						try {
							Model.getSingleton().getOptionsParam().getConfig().save();
						} catch (ConfigurationException ex) {
							logger.error(ex.getMessage(), ex);
						}
					} else {
						((JToggleButton)e.getSource()).setSelected(true);
					}
				}
			});
		}
		return btnExpandSites;
	}

	private JToggleButton getBtnExpandReports() {
		if (btnExpandReports == null) {
			btnExpandReports = new JToggleButton();
			btnExpandReports.setIcon(new ImageIcon(MainToolbarPanel.class.getResource("/resource/icon/expand_info.png")));
			btnExpandReports.setToolTipText(Constant.messages.getString("view.toolbar.expandInfo"));

			btnExpandReports.addActionListener(new java.awt.event.ActionListener() { 

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (Model.getSingleton().getOptionsParam().getViewParam().getDisplayOption() != View.DISPLAY_OPTION_BOTTOM_FULL) {
						getBtnExpandSites().setSelected(false);
						
						View.getSingleton().getMainFrame().changeDisplayOption(View.DISPLAY_OPTION_BOTTOM_FULL);
						try {
							Model.getSingleton().getOptionsParam().getConfig().save();
						} catch (ConfigurationException ex) {
							logger.error(ex.getMessage(), ex);
						}
					} else {
						((JToggleButton)e.getSource()).setSelected(true);
					}
				}
			});
		}
		return btnExpandReports;
	}
	
	public void setDisplayOption(int option) {
		if (option == View.DISPLAY_OPTION_BOTTOM_FULL) {
			btnExpandSites.setSelected(false);
			btnExpandReports.setSelected(true);
		} else if (option == View.DISPLAY_OPTION_LEFT_FULL) {
			btnExpandSites.setSelected(true);
			btnExpandReports.setSelected(false);
		}
	}

	public void sessionChanged(Session session) {
		if (session != null) {
			this.getBtnSave().setEnabled(session.isNewState());
			this.getBtnSnapshot().setEnabled(!session.isNewState());
		}
	}


}