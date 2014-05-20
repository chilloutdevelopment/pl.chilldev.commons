/**
 * This file is part of the ChillDev-Commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2014 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package pl.chilldev.commons.concurrent.collections;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Grupped messages container.
 *
 * <p>
 * All operations done on objects of this class are synchronized.
 * </p>
 *
 * <p>
 * Note that after any cleanup types keys are left in the container, just with empty messages lists.
 * </p>
 *
 * @version 0.0.2
 * @since 0.0.2
 */
public class MessageBag
    implements
        Serializable
{
    /**
     * Internal message abstraction.
     *
     * @version 0.0.2
     * @since 0.0.2
     */
    public static class Message
        implements
            Serializable
    {
        /**
         * Message content.
         */
        protected String content;

        /**
         * Creation date.
         */
        protected Date createdAt = new Date();

        /**
         * Initializes message.
         *
         * @param content Message content.
         * @since 0.0.2
         */
        public Message(String content)
        {
            this.content = content;
        }

        /**
         * Returns message content.
         *
         * @return Message content.
         * @since 0.0.2
         */
        public String getContent()
        {
            return this.content;
        }

        /**
         * Returns message creation date.
         *
         * @return Message creation date.
         * @since 0.0.2
         */
        public Date getCreatedAt()
        {
            return this.createdAt;
        }
    }

    /**
     * Internal messages storage.
     *
     * <p>
     * No need to use synchronized class as all external calls are synchronized by MessageBag class.
     * </p>
     */
    protected Map<String, List<MessageBag.Message>> messages = new HashMap<>();

    /**
     * Adds message to container.
     *
     * @param type Message type.
     * @param message Message content.
     * @since 0.0.2
     */
    public synchronized void addMessage(String type, String message)
    {
        this.ensureExists(type);

        this.messages.get(type).add(new MessageBag.Message(message));
    }

    /**
     * Checks if container has any messages.
     *
     * @return Whether there are any messages in the container.
     * @since 0.0.2
     */
    public synchronized boolean hasMessages()
    {
        for (List<MessageBag.Message> list : this.messages.values()) {
            if (!list.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if container has messages of given type.
     *
     * @param type Messages type.
     * @return Whether there are any messages of given type in the container.
     * @since 0.0.2
     */
    public synchronized boolean hasMessages(String type)
    {
        this.ensureExists(type);

        return !this.messages.get(type).isEmpty();
    }

    /**
     * Returns all messages of given type and clears container for that type.
     *
     * @param type Messages type.
     * @return All messages of given type. Always returns the lest, at least empty.
     * @since 0.0.2
     */
    public synchronized List<MessageBag.Message> getMessages(String type)
    {
        this.ensureExists(type);

        // move all the messages to new list and clear internal container
        List<MessageBag.Message> messages = new ArrayList<MessageBag.Message>(this.messages.get(type));
        this.messages.get(type).clear();

        return messages;
    }

    /**
     * Returns all messages and clears the container.
     *
     * @return All messages groupped by type. Always returns the map, at least empty.
     * @since 0.0.2
     */
    public synchronized Map<String, List<MessageBag.Message>> getAllMessages()
    {
        Map<String, List<MessageBag.Message>> messages = new HashMap<>();

        for (Map.Entry<String, List<MessageBag.Message>> entry : this.messages.entrySet()) {
            // move all the messages to new list and clear internal container
            List<MessageBag.Message> list = new ArrayList<>(entry.getValue());
            entry.getValue().clear();

            // place sub-container in full map
            messages.put(entry.getKey(), list);
        }

        return messages;
    }

    /**
     * Ensures that given sub-list exists.
     *
     * @param type Message type.
     * @since 0.0.2
     */
    protected void ensureExists(String type)
    {
        if (!this.messages.containsKey(type)) {
            this.messages.put(type, new ArrayList<MessageBag.Message>());
        }
    }
}