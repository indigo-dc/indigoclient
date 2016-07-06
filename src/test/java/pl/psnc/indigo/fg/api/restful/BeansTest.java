package pl.psnc.indigo.fg.api.restful;

import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.jaxb.Application;
import pl.psnc.indigo.fg.api.restful.jaxb.Application.Outcome;
import pl.psnc.indigo.fg.api.restful.jaxb.Infrastructure;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Link;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Parameter;
import pl.psnc.indigo.fg.api.restful.jaxb.Root;
import pl.psnc.indigo.fg.api.restful.jaxb.RuntimeData;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;
import pl.psnc.indigo.fg.api.restful.jaxb.Version;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This class holds tests for beans used in communication with FG.
 */
public class BeansTest {
    @Test
    public void testApplication() {
        Application application = new Application();
        application.setId("Id");
        application.setDescription("Description");
        application.setName("Name");
        application.setDate(new Date(0));
        application.setInfrastructures(Collections.emptyList());
        application.setOutcome(Outcome.JOB);
        application.setEnabled(true);
        application.setParameters(Collections.emptyList());
        assertEquals(
                "Application[id=Id,description=Description,name=Name,date=Thu "
                + "Jan 01 01:00:00 CET 1970,infrastructures=[],outcome=JOB,"
                + "enabled=true,parameters=[]]", application.toString());
    }

    @Test
    public void testInfrastructure() {
        Infrastructure infrastructure = new Infrastructure();
        infrastructure.setId("Id");
        infrastructure.setName("Name");
        infrastructure.setDescription("Description");
        infrastructure.setDate(new Date(0));
        infrastructure.setEnabled(true);
        infrastructure.setVirtual(true);
        infrastructure.setParameters(Collections.emptyList());
        assertEquals("Infrastructure[id=Id,name=Name,description=Description,"
                     + "date=Thu Jan 01 01:00:00 CET 1970,enabled=true,"
                     + "virtual=true,parameters=[]]",
                     infrastructure.toString());
    }

    @Test
    public void testInputFile() {
        InputFile inputFile = new InputFile();
        inputFile.setName("Name");
        inputFile.setStatus("Status");
        assertEquals("InputFile[name=Name,status=Status]",
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
    public void testLink() {
        Link link = new Link();
        link.setHref("Href");
        link.setRel("Rel");
        assertEquals("Link[rel=Rel,href=Href]", link.toString());
    }

    @Test
    public void testOutputFile() {
        OutputFile outputFile = new OutputFile();
        outputFile.setName("Name");
        outputFile.setUrl(URI.create("protocol://host:port/path"));
        assertEquals("OutputFile[name=Name,url=protocol://host:port/path]",
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
    public void testParameter() {
        Parameter parameter = new Parameter();
        parameter.setName("Name");
        parameter.setValue("Value");
        parameter.setDescription("Description");
        assertEquals("Parameter[name=Name,value=Value,description=Description]",
                     parameter.toString());

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
    public void testRoot() {
        Root root = new Root();
        root.setLinks(Collections.emptyList());
        root.setVersions(Collections.emptyList());
        assertEquals("Root[links=[],versions=[]]", root.toString());

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
    public void testRuntimeDate() {
        RuntimeData runtimeData = new RuntimeData();
        runtimeData.setName("Name");
        runtimeData.setValue("Value");
        runtimeData.setDescription("Description");
        runtimeData.setCreation("Creation");
        runtimeData.setDescription("Description");
        runtimeData.setLastChange("LastChange");
        assertEquals(
                "RuntimeData[name=Name,value=Value,description=Description,"
                + "creation=Creation,lastChange=LastChange]",
                runtimeData.toString());

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
    public void testTask() {
        Task task = new Task();
        assertEquals(task, task);
        assertNotEquals(task, null);
        assertNotEquals(task, "");
    }

    @Test
    public void testUpload() {
        Upload upload = new Upload();
        upload.setFiles(Collections.emptyList());
        upload.setMessage("Message");
        upload.setTask("Task");
        upload.setStatus("Status");
        assertEquals("Upload[files=[],message=Message,task=Task,status=Status]",
                     upload.toString());

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
    public void testVersion() {
        Version version = new Version();
        version.setStatus("Status");
        version.setUpdated("Updated");
        version.setBuild("Build");
        version.setMediaType(MediaType.WILDCARD_TYPE);
        version.setLinks(Collections.emptyList());
        version.setId("Id");
        assertEquals("Version[status=Status,updated=Updated,build=Build,"
                     + "mediaType=*/*,links=[],id=Id]", version.toString());

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
