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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

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
        assertThat("Application("
                     + "id=Id, "
                     + "name=Name, "
                     + "description=Description, "
                     + "creation=" + now + ", "
                     + "parameters=[], "
                     + "inputFiles=[], "
                     + "infrastructures=[], "
                     + "outcome=JOB, "
                     + "enabled=true, "
                     + "links=[])", is(application.toString()));
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

        assertThat(application, is(application));
        assertThat(application, is(other));
        assertThat(application.hashCode(), is(other.hashCode()));

        assertThat(application, not(is((Application) null)));
        assertThat(application, not(is("")));
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
        assertThat("Infrastructure("
                     + "id=Id, "
                     + "name=Name, "
                     + "description=Description, "
                     + "creation=" + now + ", "
                     + "parameters=[], "
                     + "enabled=true, "
                     + "virtual=true)", is(infrastructure.toString()));
        // @formatter:on

        Infrastructure other = new Infrastructure();
        other.setId(infrastructure.getId());
        other.setName(infrastructure.getName());
        other.setDescription(infrastructure.getDescription());
        other.setCreation(infrastructure.getCreation());
        other.setParameters(infrastructure.getParameters());
        other.setEnabled(infrastructure.isEnabled());
        other.setVirtual(infrastructure.isVirtual());

        assertThat(infrastructure, is(infrastructure));
        assertThat(infrastructure, is(other));
        assertThat(infrastructure.hashCode(), is(other.hashCode()));

        assertThat(infrastructure, not(is((Infrastructure) null)));
        assertThat(infrastructure, not(is("")));
    }

    @Test
    public final void testInputFile() {
        InputFile inputFile = new InputFile();
        inputFile.setName("Name");
        inputFile.setStatus("TaskStatus");
        assertThat("InputFile(name=Name, status=TaskStatus)",
                   is(inputFile.toString()));

        InputFile other = new InputFile();
        other.setName(inputFile.getName());
        other.setStatus(inputFile.getStatus());

        assertThat(inputFile, is(inputFile));
        assertThat(inputFile, is(other));
        assertThat(inputFile.hashCode(), is(other.hashCode()));

        assertThat(inputFile, not(is((InputFile) null)));
        assertThat(inputFile, not(is("")));
    }

    @Test
    public final void testLink() {
        Link link = new Link();
        link.setHref("Href");
        link.setRel("Rel");
        assertThat("Link(rel=Rel, href=Href)", is(link.toString()));

        Link other = new Link();
        other.setHref(link.getHref());
        other.setRel(link.getRel());

        assertThat(link, is(link));
        assertThat(link, is(other));
        assertThat(link.hashCode(), is(other.hashCode()));

        assertThat(link, not(is((Link) null)));
        assertThat(link, not(is("")));
    }

    @Test
    public final void testOutputFile() {
        OutputFile outputFile = new OutputFile();
        outputFile.setName("Name");
        outputFile.setUrl(URI.create("protocol://host:port/path"));
        assertThat("OutputFile(name=Name, url=protocol://host:port/path)",
                   is(outputFile.toString()));

        OutputFile other = new OutputFile();
        other.setName(outputFile.getName());
        other.setUrl(outputFile.getUrl());

        assertThat(outputFile, is(outputFile));
        assertThat(outputFile, is(other));
        assertThat(outputFile.hashCode(), is(other.hashCode()));

        assertThat(outputFile, not(is((OutputFile) null)));
        assertThat(outputFile, not(is("")));
    }

    @Test
    public final void testParameter() {
        Parameter parameter = new Parameter();
        parameter.setName("Name");
        parameter.setValue("Value");
        parameter.setDescription("Description");
        // @formatter:off
        assertThat("Parameter("
                     + "name=Name, "
                     + "value=Value, "
                     + "description=Description)", is(parameter.toString()));
        // @formatter:on

        Parameter other = new Parameter();
        other.setName(parameter.getName());
        other.setValue(parameter.getValue());
        other.setDescription(parameter.getDescription());

        assertThat(parameter, is(parameter));
        assertThat(parameter, is(other));
        assertThat(parameter.hashCode(), is(other.hashCode()));

        assertThat(parameter, not(is((Parameter) null)));
        assertThat(parameter, not(is("")));
    }

    @Test
    public final void testRoot() {
        Root root = new Root();
        root.setLinks(Collections.emptyList());
        root.setVersions(Collections.emptyList());
        assertThat("Root(links=[], versions=[])", is(root.toString()));

        Root other = new Root();
        other.setLinks(root.getLinks());
        other.setVersions(root.getVersions());

        assertThat(root, is(root));
        assertThat(root, is(other));
        assertThat(root.hashCode(), is(other.hashCode()));

        assertThat(root, not(is((Root) null)));
        assertThat(root, not(is("")));
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
        assertThat("RuntimeData("
                     + "name=Name, "
                     + "value=Value, "
                     + "description=Description, "
                     + "creation=Creation, "
                     + "lastChange=" + now + ')', is(runtimeData.toString()));
        // @formatter:on

        RuntimeData other = new RuntimeData();
        other.setName(runtimeData.getName());
        other.setValue(runtimeData.getValue());
        other.setDescription(runtimeData.getDescription());
        other.setCreation(runtimeData.getCreation());
        other.setLastChange(runtimeData.getLastChange());

        assertThat(runtimeData, is(runtimeData));
        assertThat(runtimeData, is(other));
        assertThat(runtimeData.hashCode(), is(other.hashCode()));

        assertThat(runtimeData, not(is((RuntimeData) null)));
        assertThat(runtimeData, not(is("")));
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
        assertThat("Task("
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
                     + "links=[])", is(task.toString()));
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

        assertThat(task, is(task));
        assertThat(task, is(other));
        assertThat(task.hashCode(), is(other.hashCode()));

        assertThat(task, not(is((Task) null)));
        assertThat(task, not(is("")));
    }

    @Test
    public final void testUpload() {
        Upload upload = new Upload();
        upload.setFiles(Collections.emptyList());
        upload.setMessage("Message");
        upload.setTask("Task");
        upload.setStatus("TaskStatus");
        // @formatter:off
        assertThat("Upload(files=[], "
                     + "message=Message, "
                     + "task=Task, "
                     + "status=TaskStatus)", is(upload.toString()));
        // @formatter:on

        Upload other = new Upload();
        other.setFiles(upload.getFiles());
        other.setMessage(upload.getMessage());
        other.setTask(upload.getTask());
        other.setStatus(upload.getStatus());

        assertThat(upload, is(upload));
        assertThat(upload, is(other));
        assertThat(upload.hashCode(), is(other.hashCode()));

        assertThat(upload, not(is((Upload) null)));
        assertThat(upload, not(is("")));
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
        assertThat("Version(status=TaskStatus, "
                     + "updated=Updated, "
                     + "build=Build, "
                     + "mediaType=*/*, "
                     + "links=[], "
                     + "id=Id)", is(version.toString()));
        // @formatter:on

        Version other = new Version();
        other.setStatus(version.getStatus());
        other.setUpdated(version.getUpdated());
        other.setBuild(version.getBuild());
        other.setMediaType(version.getMediaType());
        other.setLinks(version.getLinks());
        other.setId(version.getId());

        assertThat(version, is(version));
        assertThat(version, is(other));
        assertThat(version.hashCode(), is(other.hashCode()));

        assertThat(version, not(is((Version) null)));
        assertThat(version, not(is("")));
    }
}
