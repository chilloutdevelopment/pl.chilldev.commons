/*
 * This file is part of the pl.wrzasq.commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2020 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package pl.wrzasq.commons.aws.cloudformation.macro;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CloudFormation event request structure.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CloudFormationMacroRequest {
    /**
     * Request ID.
     */
    private String requestId;

    /**
     * Template fragment.
     */
    private Map<String, Object> fragment;
}
