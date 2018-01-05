/**
 * This file is part of the ChillDev-Commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2017 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package pl.chilldev.commons.data;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedResources;

/**
 * Conversion utils for Spring Data collections.
 */
public class ConvertUtils
{
    /**
     * Extracts URL string values from sorting model.
     *
     * @param sort Spring Data sort model.
     * @return URL values.
     */
    public static Collection<String> extractSort(Sort sort)
    {
        if (sort == null) {
            return null;
        }

        Collection<String> values = new ArrayList<>();

        for (Sort.Order criteria : sort) {
            values.add(criteria.getProperty() + "," + criteria.getDirection().name());
        }

        return values;
    }

    /**
     * Converts HATEOAS resources into Spring Data page.
     *
     * @param resources Paged resources.
     * @param request Pagination specification.
     * @param <ResourceType> Collection element type.
     * @return Paged result.
     */
    public static <ResourceType> Page<ResourceType> buildPageFromResources(
        PagedResources<? extends ResourceType> resources,
        Pageable request
    )
    {
        return new PageImpl<>(
            new ArrayList<>(resources.getContent()),
            request,
            resources.getMetadata().getTotalElements()
        );
    }
}