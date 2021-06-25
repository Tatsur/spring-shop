package com.ttsr.springshop.configuration.aop;

import lombok.Builder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.action.internal.EntityUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.logging.LogManager;

@Component
@Aspect
public class EventPublishingAspect {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @AfterReturning(value = "execution(public * org.springframework.data.repository.Repository+.save*(..))",returning = "object")
    public void setEventPublisher(JoinPoint joinPoint, Object object){
        String entityName = object.getClass().getSimpleName();
        eventPublisher.publishEvent(new EntityUpdateEvent(object));
    }

    private class EntityUpdateEvent extends ApplicationEvent {
        public EntityUpdateEvent(Object object) {
            super(object);
        }
    }

    @Component
    private class EntityUpdateEventListener implements ApplicationListener<EntityUpdateEvent> {
        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Async
        @Override
        public  void onApplicationEvent(EntityUpdateEvent entityUpdateEvent){
            logger.info("Created instance: " + entityUpdateEvent.getSource().toString());
        }
    }
}
