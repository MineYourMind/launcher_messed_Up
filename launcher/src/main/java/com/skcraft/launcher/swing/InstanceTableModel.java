/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.swing;

import com.skcraft.launcher.Instance;
import com.skcraft.launcher.InstanceList;
import com.skcraft.launcher.Launcher;
import com.skcraft.launcher.util.SharedLocale;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;

public class InstanceTableModel extends AbstractTableModel {

    private final InstanceList instances;
    private final ImageIcon instanceIcon;
    private final ImageIcon customInstanceIcon;
    private final ImageIcon downloadIcon;

    @SuppressWarnings("ConstantConditions")
    public InstanceTableModel(InstanceList instances) {
        this.instances = instances;
        instanceIcon = new ImageIcon(SwingHelper.readIconImage(Launcher.class, "instance_icon.png")
                .getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        customInstanceIcon = new ImageIcon(SwingHelper.readIconImage(Launcher.class, "custom_instance_icon.png")
                .getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        downloadIcon = new ImageIcon(SwingHelper.readIconImage(Launcher.class, "download_icon.png")
                .getScaledInstance(32, 32, Image.SCALE_SMOOTH));
    }

    public void update() {
        instances.sort();
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "";
            case 1:
                return SharedLocale.tr("launcher.modpackColumn");
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return ImageIcon.class;
            case 1:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                instances.get(rowIndex).setSelected((boolean) (Boolean) value);
                break;
            case 1:
            default:
                break;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return false;
            case 1:
                return false;
            default:
                return false;
        }
    }

    @Override
    public int getRowCount() {
        return instances.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    @SuppressWarnings({"ConstantConditions"})
    public Object getValueAt(int rowIndex, int columnIndex) {
        Instance instance;
        switch (columnIndex) {
            case 0:
                instance = instances.get(rowIndex);
                if (!instance.isLocal()) {
                    return downloadIcon;
                } else if (instance.getManifestURL() != null) {
                    File icon = new File(instance.getContentDir(), "custom_instance_icon.png");
                    if (icon.exists()) {
                        return new ImageIcon(SwingHelper.readIconImage(icon)
                                .getScaledInstance(32, 32, Image.SCALE_SMOOTH));
                    }
                    return instanceIcon;
                } else {
                    return customInstanceIcon;
                }
            case 1:
                instance = instances.get(rowIndex);
                return "<html>&nbsp;" + SwingHelper.htmlEscape(instance.getTitle()) + getAddendum(instance) + "</html>";
            default:
                return null;
        }
    }

    private String getAddendum(Instance instance) {
        if (!instance.isLocal()) {
            return "<br /> &nbsp;&nbsp;&nbsp;<span style=\"color: #969896\">" + SharedLocale.tr("launcher.notInstalledHint") + "</span>";
         } else if (!instance.isInstalled()) {
            return "<br /> &nbsp;&nbsp;&nbsp;<span style=\"color: #969896\">" + SharedLocale.tr("launcher.requiresUpdateHint") + "</span>";
        } else if (instance.isUpdatePending()) {
            return "<br /> &nbsp;&nbsp;&nbsp;<span style=\"color: #969896\">" + SharedLocale.tr("launcher.updatePendingHint") + "</span>";
        } else {
            return "";
        }
    }

}
