package com.actian.services.knime.useragent.node;

/*
		Copyright 2015 Actian Corporation

		Licensed under the Apache License, Version 2.0 (the "License");
		you may not use this file except in compliance with the License.
		You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

		Unless required by applicable law or agreed to in writing, software
		distributed under the License is distributed on an "AS IS" BASIS,
		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		See the License for the specific language governing permissions and
		limitations under the License.
*/


import com.actian.services.dataflow.operators.UserAgentParser;
import com.pervasive.datarush.knime.coreui.common.*;
import com.pervasive.datarush.ports.PortMetadata;
import com.pervasive.datarush.ports.record.RecordMetadata;
import com.pervasive.datarush.types.Field;
import com.pervasive.datarush.types.RecordTokenType;
import com.pervasive.datarush.types.TokenTypeConstant;
import org.knime.core.node.InvalidSettingsException;

import javax.swing.*;
import java.awt.*;

/*package*/ final class UserAgentParserNodeDialogPane implements CustomDialogComponent<UserAgentParser> {

	private final UserAgentParserNodeSettings settings = new UserAgentParserNodeSettings();

	private JComboBox<String> srcFieldList;
	private JTextField fieldPrefix;

	@Override
	public UserAgentParserNodeSettings getSettings() {
		return settings;
	}

	@Override
	public boolean isMetadataRequiredForConfiguration(int portIndex) {
		return true;
	}

	@Override
	public Component getComponent() {
		JPanel dialog = new JPanel();
		dialog.setLayout(new BorderLayout());

        JPanel fieldselector = new JPanel();
        fieldselector.setLayout(new GridLayout(2,2));

		JLabel inputFieldLabel = new JLabel("User Agent Field");
		srcFieldList = new JComboBox<String>();
		JLabel fieldPrefixLabel = new JLabel("Output field name prefix");
		fieldPrefix = new JTextField();

        fieldselector.add(inputFieldLabel);
        fieldselector.add(srcFieldList);
        fieldselector.add(fieldPrefixLabel);
        fieldselector.add(fieldPrefix);

        dialog.add(fieldselector, BorderLayout.NORTH);

		return dialog;
	}

	@Override
	public void refresh(PortMetadata[] arg0) {
		RecordTokenType inputType = ((RecordMetadata)arg0[0]).getType();
		srcFieldList.removeAllItems();
		for(Field f : inputType) {
			if (f.getType().equals(TokenTypeConstant.STRING)) {
				srcFieldList.addItem(f.getName());
			}
		}
		srcFieldList.setSelectedItem(settings.inputField.getStringValue());
		fieldPrefix.setText(settings.fieldPrefix.getStringValue());
	}

    @Override
    public void validateAndApplySettings() throws InvalidSettingsException {
        settings.inputField.setStringValue((String) srcFieldList.getSelectedItem().toString());
        settings.fieldPrefix.setStringValue(fieldPrefix.getText());
    }
}


