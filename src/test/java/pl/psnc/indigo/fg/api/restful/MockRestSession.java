package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockRestSession {
    public static final URI MOCK_ADDRESS = URI.create("http://mock:8888");

    private static final String APPLICATIONS = "applications";
    private static final Task MOCK_TASK = new Task();

    static {
        MockRestSession.MOCK_TASK.setUser("brunor");
        MockRestSession.MOCK_TASK.setApplication("1");
        MockRestSession.MOCK_TASK.setDescription("hello");
    }

    public static Task mockTask() {
        return MockRestSession.MOCK_TASK;
    }

    private final Client client = mock(Client.class);
    private final ClassLoader classLoader = getClass().getClassLoader();
    private final ObjectMapper mapper = new ObjectMapper();

    public MockRestSession() throws IOException, JsonProcessingException {
        super();
        mockRootAPI();
        mockApplicationsAPI();
        mockTasksAPI();
    }

    private void mockTasksAPI() throws IOException, JsonProcessingException {
        URI uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                            .path("tasks").queryParam("user", "all-tasks")
                            .build();
        String body = readResource("tasks.json");
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("1").build();
        body = readResource("tasks_1.json");
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("2").build();
        body = readResource("tasks_2.json");
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("invalid-uri").build();
        mockGetPostResponse(uri, Response.Status.NOT_FOUND, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("invalid-body").build();
        body = "invalid-JSON-body";
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("file").queryParam("path", "/tmp")
                        .queryParam("name", "test.txt").build();
        InputStream streamBody =
                IOUtils.toInputStream("TEST", Charset.defaultCharset());
        mockGetPostResponse(uri, Response.Status.OK, streamBody);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("file").queryParam("path", "/tmp")
                        .queryParam("name", "non-existing-file").build();
        mockGetPostResponse(uri, Response.Status.NOT_FOUND, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").queryParam("user", "brunor").build();
        body = mapper.writeValueAsString(MockRestSession.mockTask());
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").queryParam("user", "invalid-uri")
                        .build();
        mockGetPostResponse(uri, Response.Status.FORBIDDEN, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").queryParam("user", "invalid-body")
                        .build();
        mockGetPostResponse(uri, Response.Status.OK, "invalid-JSON-body");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("1").path("input")
                        .queryParam("user", "brunor").build();
        body = mapper.writeValueAsString(new Upload());
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("1").path("input")
                        .queryParam("user", "invalid-uri").build();
        mockGetPostResponse(uri, Response.Status.FORBIDDEN, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("1").path("input")
                        .queryParam("user", "invalid-body").build();
        body = "invalid-JSON-body";
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("non-existing-task").build();
        mockDeleteResponse(uri, Response.Status.NOT_FOUND);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("existing-task").build();
        mockDeleteResponse(uri, Response.Status.OK);
    }

    private void mockApplicationsAPI() throws IOException {
        URI uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                            .path(MockRestSession.APPLICATIONS).build();
        String body = readResource("applications.json");
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path(MockRestSession.APPLICATIONS).path("1").build();
        body = readResource("applications_1.json");
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path(MockRestSession.APPLICATIONS).path("invalid-uri")
                        .build();
        mockGetPostResponse(uri, Response.Status.NOT_FOUND, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path(MockRestSession.APPLICATIONS).path("invalid-body")
                        .build();
        body = "invalid-JSON-body";
        mockGetPostResponse(uri, Response.Status.OK, body);
    }

    private void mockRootAPI() throws IOException {
        URI uri = MockRestSession.MOCK_ADDRESS;
        String body = readResource("root.json");
        mockGetPostResponse(uri, Response.Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS)
                        .path("invalid-uri").build();
        mockGetPostResponse(uri, Response.Status.NOT_FOUND, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS)
                        .path("invalid-body").build();
        body = "invalid-JSON-body";
        mockGetPostResponse(uri, Response.Status.OK, body);
    }

    public final Client getClient() {
        return client;
    }

    public final void mockGetPostResponse(
            final URI uri, final Response.StatusType status,
            final Object body) {
        Response response = mock(Response.class);
        when(response.getStatusInfo()).thenReturn(status);
        when(response.readEntity(any(Class.class))).thenReturn(body);

        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(builder.accept(any(MediaType.class))).thenReturn(builder);
        when(builder.header(anyString(), any())).thenReturn(builder);
        when(builder.post(any(Entity.class))).thenReturn(response);
        when(builder.get()).thenReturn(response);

        WebTarget target = mock(WebTarget.class);
        when(target.request(any(MediaType.class))).thenReturn(builder);
        when(target.request()).thenReturn(builder);

        when(client.target(uri)).thenReturn(target);
    }

    public final void mockDeleteResponse(
            final URI uri, final Response.StatusType status) {
        Response response = mock(Response.class);
        when(response.getStatusInfo()).thenReturn(status);

        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(builder.accept(any(MediaType.class))).thenReturn(builder);
        when(builder.header(anyString(), any())).thenReturn(builder);
        when(builder.delete()).thenReturn(response);

        WebTarget target = mock(WebTarget.class);
        when(target.request(any(MediaType.class))).thenReturn(builder);
        when(target.request()).thenReturn(builder);

        when(client.target(uri)).thenReturn(target);
    }

    private String readResource(final String resource) throws IOException {
        try (InputStream stream = classLoader.getResourceAsStream(resource)) {
            return IOUtils.toString(stream, Charset.defaultCharset());
        }
    }
}
