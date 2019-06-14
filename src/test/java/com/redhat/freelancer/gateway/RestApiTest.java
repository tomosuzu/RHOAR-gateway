package com.redhat.freelancer.gateway;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import io.restassured.RestAssured;
import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.mapper.TypeRef;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class RestApiTest {

    @Rule
    public WireMockRule serviceMock = new WireMockRule(18081);

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, RestApplication.class.getPackage())
                .addAsResource("project-local.yml", "project-defaults.yml");
    }

    @Before
    public void beforeTest() {
        String port = "18080";
        RestAssured.baseURI = String.format("http://localhost:%s", port);
    }

    @Test
    @RunAsClient
    public void testGetProjects() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("projects-response.json");
        serviceMock.stubFor(get(urlEqualTo("/projects")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(IOUtils.toString(is))));


        List<Map<String, Object>> projects = given()
                .get("/projects")
                .as(new TypeRef<List<Map<String, Object>>>() {});


        assertThat(projects, hasSize(2));
        assertThat(projects.get(0).get("projectId"), equalTo("211111"));
        assertThat(projects.get(0).get("firstName"), equalTo("firstName1"));
        assertThat(projects.get(0).get("lastName"), equalTo("lastName1"));
        assertThat(projects.get(0).get("emailAddress"), equalTo("emailAddress1"));
        assertThat(projects.get(0).get("title"), equalTo("title1"));
        assertThat(projects.get(0).get("desc"), equalTo("description1"));
        assertThat(projects.get(0).get("status"), equalTo("open"));
        assertThat(projects.get(1).get("projectId"), equalTo("211112"));
        assertThat(projects.get(1).get("firstName"), equalTo("firstName2"));
        assertThat(projects.get(1).get("lastName"), equalTo("lastName2"));
        assertThat(projects.get(1).get("emailAddress"), equalTo("emailAddress2"));
        assertThat(projects.get(1).get("title"), equalTo("title2"));
        assertThat(projects.get(1).get("desc"), equalTo("description2"));
        assertThat(projects.get(1).get("status"), equalTo("in_progress"));
    }

    @Test
    @RunAsClient
    public void testGetProject() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("project-response.json");
        serviceMock.stubFor(get(urlEqualTo("/projects/211111")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(IOUtils.toString(is))));


        List<Map<String, Object>> projects = given()
                .get("/projects/211111")
                .as(new TypeRef<List<Map<String, Object>>>() {});


        assertThat(projects, hasSize(1));
        assertThat(projects.get(0).get("projectId"), equalTo("211111"));
        assertThat(projects.get(0).get("firstName"), equalTo("firstName1"));
        assertThat(projects.get(0).get("lastName"), equalTo("lastName1"));
        assertThat(projects.get(0).get("emailAddress"), equalTo("emailAddress1"));
        assertThat(projects.get(0).get("title"), equalTo("title1"));
        assertThat(projects.get(0).get("desc"), equalTo("description1"));
        assertThat(projects.get(0).get("status"), equalTo("open"));
    }

    @Test
    @RunAsClient
    public void testGetProjectStatus() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("project-status-response.json");
        serviceMock.stubFor(get(urlEqualTo("/projects/status/open")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(IOUtils.toString(is))));


        List<Map<String, Object>> projects = given()
                .get("/projects/status/open")
                .as(new TypeRef<List<Map<String, Object>>>() {});


        assertThat(projects, hasSize(1));
        assertThat(projects.get(0).get("projectId"), equalTo("211111"));
        assertThat(projects.get(0).get("firstName"), equalTo("firstName1"));
        assertThat(projects.get(0).get("lastName"), equalTo("lastName1"));
        assertThat(projects.get(0).get("emailAddress"), equalTo("emailAddress1"));
        assertThat(projects.get(0).get("title"), equalTo("title1"));
        assertThat(projects.get(0).get("desc"), equalTo("description1"));
        assertThat(projects.get(0).get("status"), equalTo("open"));
    }

    @Test
    @RunAsClient
    public void testGetFreelancers() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("freelancers-response.json");
        serviceMock.stubFor(get(urlEqualTo("/freelancers")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(IOUtils.toString(is))));


        List<Map<String, Object>> freelancers = given()
                .get("/freelancers")
                .as(new TypeRef<List<Map<String, Object>>>() {});


        assertThat(freelancers, hasSize(2));
        assertThat(freelancers.get(0).get("freelancerId"), equalTo("333333"));
        assertThat(freelancers.get(0).get("firstName"), equalTo("firstName1"));
        assertThat(freelancers.get(0).get("lastName"), equalTo("lastName1"));
        assertThat(freelancers.get(0).get("emailAddress"), equalTo("emailAddress1"));
        assertThat(freelancers.get(0).get("skillList"), equalTo("skillList1"));
        assertThat(freelancers.get(1).get("freelancerId"), equalTo("333334"));
        assertThat(freelancers.get(1).get("firstName"), equalTo("firstName2"));
        assertThat(freelancers.get(1).get("lastName"), equalTo("lastName2"));
        assertThat(freelancers.get(1).get("emailAddress"), equalTo("emailAddress2"));
        assertThat(freelancers.get(1).get("skillList"), equalTo("skillList2"));
    }

    @Test
    @RunAsClient
    public void testGetFreelancer() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("freelancer-response.json");
        serviceMock.stubFor(get(urlEqualTo("/freelancers/333333")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(IOUtils.toString(is))));


       Map<String, Object> freelancer = given()
                .get("/freelancers/333333")
                .as(new TypeRef<Map<String, Object>>() {});


        assertThat(freelancer.get("freelancerId"), equalTo("333333"));
        assertThat(freelancer.get("firstName"), equalTo("firstName1"));
        assertThat(freelancer.get("lastName"), equalTo("lastName1"));
        assertThat(freelancer.get("emailAddress"), equalTo("emailAddress1"));
        assertThat(freelancer.get("skillList"), equalTo("skillList1"));
    }
}
