package pl.psnc.indigo.fg.api.restful;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import pl.psnc.indigo.fg.api.restful.category.UnitTests;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;
import pl.psnc.indigo.fg.api.restful.jaxb.Infrastructure;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Link;
import pl.psnc.indigo.fg.api.restful.jaxb.Outcome;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Parameter;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;
import pl.psnc.indigo.fg.api.restful.jaxb.RuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.TaskStatus;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;
import pl.psnc.indigo.fg.api.restful.jaxb.Version;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This class holds tests for beans used in communication with FG.
 */
@Category(UnitTests.class)
public class BeansTest {
    @Test
    public final void testApplication() {
        LocalDateTime now = LocalDateTime.now();

        Application application = new Application();
        application.setId("Id");
        application.setName("Name");
        application.setDescription("Description");
        application.setCreation(now);
        application.setParameters(Collections.emptyList());
        application.setInputFiles(Collections.emptyList());
        application.setInfrastructures(Collections.emptyList());
        application.setOutcome(Outcome.JOB);
        application.setEnabled(true);
        application.setLinks(Collections.emptyList());
        // @formatter:off
        assertEquals("Application("
                     + "id=Id, "
                     + "name=Name, "
                     + "description=Description, "
                     + "creation=" + now + ", "
                     + "parameters=[], "
                     + "inputFiles=[], "
                     + "infrastructures=[], "
                     + "outcome=JOB, "
                     + "enabled=true, "
                     + "links=[])", application.toString());
        // @formatter:on

        Application other = new Application();
        other.setId(application.getId());
        other.setName(application.getName());
        other.setDescription(application.getDescription());
        other.setCreation(application.getCreation());
        other.setParameters(application.getParameters());
        other.setInputFiles(application.getInputFiles());
        other.setInfrastructures(application.getInfrastructures());
        other.setOutcome(application.getOutcome());
        other.setEnabled(application.isEnabled());
        other.setLinks(application.getLinks());

        assertEquals(application, application);
        assertEquals(application, other);
        assertEquals(application.hashCode(), other.hashCode());

        assertNotEquals(application, null);
        assertNotEquals(application, "");
    }

    @Test
    public final void testInfrastructure() {
        LocalDateTime now = LocalDateTime.now();

        Infrastructure infrastructure = new Infrastructure();
        infrastructure.setId("Id");
        infrastructure.setName("Name");
        infrastructure.setDescription("Description");
        infrastructure.setCreation(now);
        infrastructure.setParameters(Collections.emptyList());
        infrastructure.setEnabled(true);
        infrastructure.setVirtual(true);
        // @formatter:off
        assertEquals("Infrastructure("
                     + "id=Id, "
                     + "name=Name, "
                     + "description=Description, "
                     + "creation=" + now + ", "
                     + "parameters=[], "
                     + "enabled=true, "
                     + "virtual=true)",
                     infrastructure.toString());
        // @formatter:on

        Infrastructure other = new Infrastructure();
        other.setId(infrastructure.getId());
        other.setName(infrastructure.getName());
        other.setDescription(infrastructure.getDescription());
        other.setCreation(infrastructure.getCreation());
        other.setParameters(infrastructure.getParameters());
        other.setEnabled(infrastructure.isEnabled());
        other.setVirtual(infrastructure.isVirtual());

        assertEquals(infrastructure, infrastructure);
        assertEquals(infrastructure, other);
        assertEquals(infrastructure.hashCode(), other.hashCode());

        assertNotEquals(infrastructure, null);
        assertNotEquals(infrastructure, "");
    }

    @Test
    public final void testInputFile() {
        InputFile inputFile = new InputFile();
        inputFile.setName("Name");
        inputFile.setStatus("TaskStatus");
        assertEquals("InputFile(name=Name, status=TaskStatus)",
                     inputFile.toString());

        InputFile other = new InputFile();
        other.setName(inputFile.getName());
        other.setStatus(inputFile.getStatus());

        assertEquals(inputFile, inputFile);
        assertEquals(inputFile, other);
        assertEquals(inputFile.hashCode(), other.hashCode());

        assertNotEquals(inputFile, null);
        assertNotEquals(inputFile, "");
    }

    @Test
    public final void testLink() {
        Link link = new Link();
        link.setHref("Href");
        link.setRel("Rel");
        assertEquals("Link(rel=Rel, href=Href)", link.toString());

        Link other = new Link();
        other.setHref(link.getHref());
        other.setRel(link.getRel());

        assertEquals(link, link);
        assertEquals(link, other);
        assertEquals(link.hashCode(), other.hashCode());

        assertNotEquals(link, null);
        assertNotEquals(link, "");
    }

    @Test
    public final void testOutputFile() {
        OutputFile outputFile = new OutputFile();
        outputFile.setName("Name");
        outputFile.setUrl(URI.create("protocol://host:port/path"));
        assertEquals("OutputFile(name=Name, url=protocol://host:port/path)",
                     outputFile.toString());

        OutputFile other = new OutputFile();
        other.setName(outputFile.getName());
        other.setUrl(outputFile.getUrl());

        assertEquals(outputFile, outputFile);
        assertEquals(outputFile, other);
        assertEquals(outputFile.hashCode(), other.hashCode());

        assertNotEquals(outputFile, null);
        assertNotEquals(outputFile, "");
    }

    @Test
    public final void testParameter() {
        Parameter parameter = new Parameter();
        parameter.setName("Name");
        parameter.setValue("Value");
        parameter.setDescription("Description");
        // @formatter:off
        assertEquals("Parameter("
                     + "name=Name, "
                     + "value=Value, "
                     + "description=Description)", parameter.toString());
        // @formatter:on

        Parameter other = new Parameter();
        other.setName(parameter.getName());
        other.setValue(parameter.getValue());
        other.setDescription(parameter.getDescription());

        assertEquals(parameter, parameter);
        assertEquals(parameter, other);
        assertEquals(parameter.hashCode(), other.hashCode());

        assertNotEquals(parameter, null);
        assertNotEquals(parameter, "");
    }

    @Test
    public final void testRoot() {
        Root root = new Root();
        root.setLinks(Collections.emptyList());
        root.setVersions(Collections.emptyList());
        assertEquals("Root(links=[], versions=[])", root.toString());

        Root other = new Root();
        other.setLinks(root.getLinks());
        other.setVersions(root.getVersions());

        assertEquals(root, root);
        assertEquals(root, other);
        assertEquals(root.hashCode(), other.hashCode());

        assertNotEquals(root, null);
        assertNotEquals(root, "");
    }

    @Test
    public final void testRuntimeDate() {
        LocalDateTime now = LocalDateTime.now();

        RuntimeData runtimeData = new RuntimeData();
        runtimeData.setName("Name");
        runtimeData.setValue("Value");
        runtimeData.setDescription("Description");
        runtimeData.setCreation("Creation");
        runtimeData.setDescription("Description");
        runtimeData.setLastChange(now);
        // @formatter:off
        assertEquals("RuntimeData("
                     + "name=Name, "
                     + "value=Value, "
                     + "description=Description, "
                     + "creation=Creation, "
                     + "lastChange=" + now + ")", runtimeData.toString());
        // @formatter:on

        RuntimeData other = new RuntimeData();
        other.setName(runtimeData.getName());
        other.setValue(runtimeData.getValue());
        other.setDescription(runtimeData.getDescription());
        other.setCreation(runtimeData.getCreation());
        other.setLastChange(runtimeData.getLastChange());

        assertEquals(runtimeData, runtimeData);
        assertEquals(runtimeData, other);
        assertEquals(runtimeData.hashCode(), other.hashCode());

        assertNotEquals(runtimeData, null);
        assertNotEquals(runtimeData, "");
    }

    @Test
    public final void testTask() {
        LocalDateTime now = LocalDateTime.now();

        Task task = new Task();
        task.setId("Id");
        task.setDate(now);
        task.setLastChange(now);
        task.setApplication("Application");
        task.setInfrastructureTask("InfrastructureTask");
        task.setDescription("Description");
        task.setStatus(TaskStatus.DONE);
        task.setUser("User");
        task.setArguments(Collections.emptyList());
        task.setInputFiles(Collections.emptyList());
        task.setOutputFiles(Collections.emptyList());
        task.setRuntimeData(Collections.emptyList());
        task.setCreation("Creation");
        task.setIosandbox("IOSandbox");
        task.setLinks(Collections.emptyList());
        // @formatter:off
        assertEquals("Task("
                     + "id=Id, "
                     + "date=" + now + ", "
                     + "lastChange=" + now + ", "
                     + "application=Application, "
                     + "infrastructureTask=InfrastructureTask, "
                     + "description=Description, "
                     + "status=DONE, "
                     + "user=User, "
                     + "arguments=[], "
                     + "inputFiles=[], "
                     + "outputFiles=[], "
                     + "runtimeData=[], "
                     + "creation=Creation, "
                     + "iosandbox=IOSandbox, "
                     + "links=[])", task.toString());
        // @formatter:on

        Task other = new Task();
        other.setId(task.getId());
        other.setDate(task.getDate());
        other.setLastChange(task.getLastChange());
        other.setApplication(task.getApplication());
        other.setInfrastructureTask(task.getInfrastructureTask());
        other.setDescription(task.getDescription());
        other.setStatus(task.getStatus());
        other.setUser(task.getUser());
        other.setArguments(task.getArguments());
        other.setInputFiles(task.getInputFiles());
        other.setOutputFiles(task.getOutputFiles());
        other.setRuntimeData(task.getRuntimeData());
        other.setCreation(task.getCreation());
        other.setIosandbox(task.getIosandbox());
        other.setLinks(task.getLinks());

        assertEquals(task, task);
        assertEquals(task, other);
        assertEquals(task.hashCode(), other.hashCode());

        assertNotEquals(task, null);
        assertNotEquals(task, "");
    }

    @Test
    public final void testUpload() {
        Upload upload = new Upload();
        upload.setFiles(Collections.emptyList());
        upload.setMessage("Message");
        upload.setTask("Task");
        upload.setStatus("TaskStatus");
        // @formatter:off
        assertEquals("Upload(files=[], "
                     + "message=Message, "
                     + "task=Task, "
                     + "status=TaskStatus)",
                     upload.toString());
        // @formatter:on

        Upload other = new Upload();
        other.setFiles(upload.getFiles());
        other.setMessage(upload.getMessage());
        other.setTask(upload.getTask());
        other.setStatus(upload.getStatus());

        assertEquals(upload, upload);
        assertEquals(upload, other);
        assertEquals(upload.hashCode(), other.hashCode());

        assertNotEquals(upload, null);
        assertNotEquals(upload, "");
    }

    @Test
    public final void testVersion() {
        Version version = new Version();
        version.setStatus("TaskStatus");
        version.setUpdated("Updated");
        version.setBuild("Build");
        version.setMediaType(MediaType.WILDCARD_TYPE);
        version.setLinks(Collections.emptyList());
        version.setId("Id");
        // @formatter:off
        assertEquals("Version(status=TaskStatus, "
                     + "updated=Updated, "
                     + "build=Build, "
                     + "mediaType=*/*, "
                     + "links=[], "
                     + "id=Id)", version.toString());
        // @formatter:on

        Version other = new Version();
        other.setStatus(version.getStatus());
        other.setUpdated(version.getUpdated());
        other.setBuild(version.getBuild());
        other.setMediaType(version.getMediaType());
        other.setLinks(version.getLinks());
        other.setId(version.getId());

        assertEquals(version, version);
        assertEquals(version, other);
        assertEquals(version.hashCode(), other.hashCode());

        assertNotEquals(version, null);
        assertNotEquals(version, "");
    }
}
