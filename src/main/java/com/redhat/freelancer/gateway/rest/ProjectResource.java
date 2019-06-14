package com.redhat.freelancer.gateway.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import com.redhat.freelancer.gateway.model.Project;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

@ApplicationScoped
@ContextName("rest-context")
public class ProjectResource extends RouteBuilder {
    @Inject
    @ConfigurationValue("projects.service.url")
    private String projectUrl;

    @Override
    public void configure() {
        restConfiguration()
                .component("undertow")
                .bindingMode(RestBindingMode.json)
                .enableCORS(true);

        rest("/projects").description("Project Service")
                .produces(MediaType.APPLICATION_JSON)

        .get("/").description("Get Project List").outType(Project[].class)
                .route().id("getProjectsRoute")
                .setBody(simple("null"))
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
                .setHeader(Exchange.HTTP_PATH, simple("projects"))
                .setHeader(Exchange.HTTP_URI, simple(projectUrl))
                .to("http4://DUMMY")
                .setHeader("CamelJacksonUnmarshalType", simple(Project[].class.getName()))
                .unmarshal()
                .json(JsonLibrary.Jackson, Project[].class)
                .endRest()

        .get("/{projectId}").description("Get a Project by Id").param()
                .name("projectId").type(RestParamType.path).description("The Id of project")
                .dataType("string").endParam().outType(Project[].class)
                .route().id("getProjectRoute")
                .setBody(simple("null"))
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
                .setHeader(Exchange.HTTP_PATH, simple("projects/${header.projectId}"))
                .setHeader(Exchange.HTTP_URI, simple(projectUrl))
                .to("http4://DUMMY")
                .setHeader("CamelJacksonUnmarshalType", simple(Project[].class.getName()))
                .unmarshal()
                .json(JsonLibrary.Jackson, Project[].class)
                .endRest()

        .get("/status/{status}").description("Get Projects by status").param()
                .name("status").type(RestParamType.path).description("The status of project")
                .dataType("string").endParam().outType(Project[].class)
                .route().id("getStatusRoute")
                .setBody(simple("null"))
                .removeHeaders("CamelHttp*")
                .setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
                .setHeader(Exchange.HTTP_PATH, simple("projects/status/${header.status}"))
                .setHeader(Exchange.HTTP_URI, simple(projectUrl))
                .to("http4://DUMMY")
                .setHeader("CamelJacksonUnmarshalType", simple(Project[].class.getName()))
                .unmarshal()
                .json(JsonLibrary.Jackson, Project[].class)
                .endRest()
        ;

    }
}