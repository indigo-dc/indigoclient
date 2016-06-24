package pl.psnc.indigo.fg.api.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("NestedMethodCall")
public class MockRestSession {
    public static final URI MOCK_ADDRESS = URI.create("http://mock:8888");

    private static Task MOCK_TASK;

    public static final Task getMockTask() {
        if (MockRestSession.MOCK_TASK == null) {
            MOCK_TASK = new Task();
            MOCK_TASK.setUser("brunor");
            MOCK_TASK.setApplication("1");
            MOCK_TASK.setDescription("hello");
        }
        return MockRestSession.MOCK_TASK;
    }

    private final Client client = mock(Client.class);
    private final ClassLoader classLoader = getClass().getClassLoader();
    private final ObjectMapper mapper = new ObjectMapper();

    public MockRestSession() throws IOException {
        mockRootAPI();
        mockApplicationsAPI();
        mockTasksAPI();
    }

    private void mockTasksAPI() throws IOException {
        URI uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                            .path("tasks").queryParam("user", "all-tasks")
                            .build();
        String body = readResource("tasks.json");
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("1").build();
        body = readResource("tasks_1.json");
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("2").build();
        body = readResource("tasks_2.json");
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("invalid-uri").build();
        mockGetPostResponse(uri, Status.NOT_FOUND, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("invalid-body").build();
        body = "invalid-JSON-body";
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("file").queryParam("path", "/tmp")
                        .queryParam("name", "test.txt").build();
        InputStream streamBody = IOUtils
                .toInputStream("TEST", Charset.defaultCharset());
        mockGetPostResponse(uri, Status.OK, streamBody);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("file").queryParam("path", "/tmp")
                        .queryParam("name", "non-existing-file").build();
        mockGetPostResponse(uri, Status.NOT_FOUND, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").queryParam("user", "brunor").build();
        body = mapper.writeValueAsString(MockRestSession.getMockTask());
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").queryParam("user", "invalid-uri")
                        .build();
        mockGetPostResponse(uri, Status.FORBIDDEN, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").queryParam("user", "invalid-body")
                        .build();
        mockGetPostResponse(uri, Status.OK, "invalid-JSON-body");

        uri = UriBuilder.fromUri(MOCK_ADDRESS).path("v1.0").path("tasks")
                        .path("1").path("input").queryParam("user", "brunor")
                        .build();
        body = mapper.writeValueAsString(new Upload());
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MOCK_ADDRESS).path("v1.0").path("tasks")
                        .path("1").path("input")
                        .queryParam("user", "invalid-uri").build();
        mockGetPostResponse(uri, Status.FORBIDDEN, "");

        uri = UriBuilder.fromUri(MOCK_ADDRESS).path("v1.0").path("tasks")
                        .path("1").path("input")
                        .queryParam("user", "invalid-body").build();
        body = "invalid-JSON-body";
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("non-existing-task").build();
        mockDeleteResponse(uri, Status.NOT_FOUND);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("tasks").path("existing-task").build();
        mockDeleteResponse(uri, Status.OK);
    }

    private void mockApplicationsAPI() throws IOException {
        URI uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                            .path("applications").build();
        String body = readResource("applications.json");
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("applications").path("1").build();
        body = readResource("applications_1.json");
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("applications").path("invalid-uri").build();
        mockGetPostResponse(uri, Status.NOT_FOUND, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS).path("v1.0")
                        .path("applications").path("invalid-body").build();
        body = "invalid-JSON-body";
        mockGetPostResponse(uri, Status.OK, body);
    }

    private void mockRootAPI() throws IOException {
        URI uri = MockRestSession.MOCK_ADDRESS;
        String body = readResource("root.json");
        mockGetPostResponse(uri, Status.OK, body);

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS)
                        .path("invalid-uri").build();
        mockGetPostResponse(uri, Status.NOT_FOUND, "");

        uri = UriBuilder.fromUri(MockRestSession.MOCK_ADDRESS)
                        .path("invalid-body").build();
        body = "invalid-JSON-body";
        mockGetPostResponse(uri, Status.OK, body);
    }

    public Client getClient() {
        return client;
    }

    private void mockGetPostResponse(URI uri, Status status, Object body) {
        Response response = mock(Response.class);
        when(response.getStatusInfo()).thenReturn(status);
        when(response.readEntity(any(Class.class))).thenReturn(body);

        Builder builder = mock(Builder.class);
        when(builder.accept(any(MediaType.class))).thenReturn(builder);
        when(builder.header(anyString(), any())).thenReturn(builder);
        when(builder.post(any(Entity.class))).thenReturn(response);
        when(builder.get()).thenReturn(response);

        WebTarget target = mock(WebTarget.class);
        when(target.request(any(MediaType.class))).thenReturn(builder);
        when(target.request()).thenReturn(builder);

        when(client.target(uri)).thenReturn(target);
    }

    private void mockDeleteResponse(URI uri, Status status) {
        Response response = mock(Response.class);
        when(response.getStatusInfo()).thenReturn(status);

        Builder builder = mock(Builder.class);
        when(builder.accept(any(MediaType.class))).thenReturn(builder);
        when(builder.header(anyString(), any())).thenReturn(builder);
        when(builder.delete()).thenReturn(response);

        WebTarget target = mock(WebTarget.class);
        when(target.request(any(MediaType.class))).thenReturn(builder);
        when(target.request()).thenReturn(builder);

        when(client.target(uri)).thenReturn(target);
    }

    private String readResource(String resource) throws IOException {
        try (InputStream stream = classLoader.getResourceAsStream(resource)) {
            return IOUtils.toString(stream, Charset.defaultCharset());
        }
    }
}
