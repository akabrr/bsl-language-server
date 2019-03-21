/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2019
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package org.github._1c_syntax.bsl.languageserver.configuration;

import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.github._1c_syntax.bsl.languageserver.configuration.diagnostics.DiagnosticConfiguration;
import org.github._1c_syntax.bsl.languageserver.configuration.diagnostics.LineLengthDiagnosticConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LanguageServerConfigurationTest {

  @Test
  void createDefault() {
    // when
    LanguageServerConfiguration configuration = LanguageServerConfiguration.create();

    // then
    assertThat(configuration.getDiagnosticLanguage()).isEqualTo(DiagnosticLanguage.RU);
    assertThat(configuration.getDiagnostics()).isEmpty();
  }

  @Test
  void createFromFile() {

    // given
    File configurationFile = new File("./src/test/resources/bsl-language-server.conf");

    // when
    LanguageServerConfiguration configuration = LanguageServerConfiguration.create(configurationFile);

    // then
    DiagnosticLanguage diagnosticLanguage = configuration.getDiagnosticLanguage();
    Map<String, Either<Boolean, DiagnosticConfiguration>> diagnostics = configuration.getDiagnostics();

    assertThat(diagnosticLanguage).isEqualTo(DiagnosticLanguage.EN);
    assertThat(diagnostics).hasSize(2);

    Either<Boolean, DiagnosticConfiguration> lineLength = diagnostics.get("LineLength");
    assertThat(lineLength.isRight()).isTrue();
    assertThat(lineLength.getRight()).isOfAnyClassIn(LineLengthDiagnosticConfiguration.class);
    assertThat((LineLengthDiagnosticConfiguration) lineLength.getRight())
      .extracting(LineLengthDiagnosticConfiguration::getMaxLineLength)
      .isEqualTo(140);

    Either<Boolean, DiagnosticConfiguration> methodSize = diagnostics.get("MethodSize");
    assertThat(methodSize.isLeft()).isTrue();
    assertThat(methodSize.getLeft()).isEqualTo(false);


  }
}