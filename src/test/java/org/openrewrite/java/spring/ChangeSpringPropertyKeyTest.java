/*
 * Copyright 2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.spring;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RewriteTest;
import static org.openrewrite.yaml.Assertions.yaml;
import static org.openrewrite.properties.Assertions.properties;


public class ChangeSpringPropertyKeyTest implements RewriteTest {

    @Test
    void changeLastKey() {
        rewriteRun(
                spec -> spec.recipe(new ChangeSpringPropertyKey("server.servlet-path", "server.servlet.path", null)),
                properties(
                        """
                        server.servlet-path=/tmp/my-server-path
                        """,
                        """
                        server.servlet.path=/tmp/my-server-path
                        """
                ),
                yaml(
                        """
                            server:
                              servlet-path: /tmp/my-server-path
                        """,
                        """
                            server:
                              servlet:
                                path: /tmp/my-server-path
                        """
                )
        );
    }

    @Test
    void changePropertyPath() {
        rewriteRun(
                spec -> spec.recipe(new ChangeSpringPropertyKey("session.cookie.path", "servlet.session.cookie.path", null)),
                properties(
                        """
                        session.cookie.path=/cookie-monster
                        """,
                        """
                        servlet.session.cookie.path=/cookie-monster
                        """
                ),
                yaml(
                        """
                            server:
                              port: 8888
                            session:
                              cookie:
                                path: /tmp/my-server-path
                              """,
                        """
                            server:
                              port: 8888
                            servlet:
                              session:
                                cookie:
                                  path: /tmp/my-server-path
                        """
                )
        );
    }

}
