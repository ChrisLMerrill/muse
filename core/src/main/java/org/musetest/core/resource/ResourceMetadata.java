package org.musetest.core.resource;

import org.musetest.core.*;

import java.util.*;

/**
 * Contains attributes about the resource that are attached by the ResourceStore.
 * I.e. the describe how it is stored, when it was changed, etc.
 *
 * Metadata is not (directly) persisted when stored and may be recreated from scratch
 * by the ResourceStore when restored.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Deprecated
@SuppressWarnings("unchecked")
public class ResourceMetadata
    {
    public ResourceMetadata()
        {
        }

    public void setSaver(MuseResourceSaver saver)
        {

        }
    }


