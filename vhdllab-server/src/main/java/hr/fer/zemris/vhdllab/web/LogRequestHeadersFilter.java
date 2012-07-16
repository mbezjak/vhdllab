/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
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
 ******************************************************************************/
package hr.fer.zemris.vhdllab.web;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.AbstractRequestLoggingFilter;

public final class LogRequestHeadersFilter extends AbstractRequestLoggingFilter {

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        if (logger.isDebugEnabled()) {
            Enumeration<?> headerNames = request.getHeaderNames();
            StringBuilder sb = new StringBuilder(2000);
            sb.append("Request headers for uri: ").append(
                    request.getRequestURI());
            sb.append("\n");
            while (headerNames.hasMoreElements()) {
                String name = (String) headerNames.nextElement();
                String value = request.getHeader(name);
                sb.append(name).append(":").append(value).append("\n");
            }
            logger.debug(sb.toString());
        }
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
    }

}
