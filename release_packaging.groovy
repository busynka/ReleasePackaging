/**
 * Created by hvainshtein on 1/6/2016.
 */
/*
* Release Packaging Tool
* @author Hanna Vainshtein
* @version 1.1
* @created_on 12/10/2015
*/

import java.util.zip.ZipEntry
import java.util.zip.ZipFile


static void main (args){
    if (args.size() < 2) {
        println "directory source_file conf_file"
    }
    else {
        zip (args[0], args[1], args[2])
    }
}

static void zip ( String dir, String source_file, String conf_file  ){

    def path = dir + source_file
    def conf_path = conf_file

    //reading zip file
    ZipFile file = new ZipFile(path)
    def branch = "Branches/"
    def project = "Projects/"
    def BranchList = []
    def ProjectList = []
    Enumeration <? extends ZipEntry> e = file.entries()
    while (e.hasMoreElements()) {
        ZipEntry entry = e.nextElement()
        def entryName = entry.getName()
        // parsing to get all the project names
        if (entryName =~ project) {
            def schedule = entryName-project
            schedule = schedule.substring(0, schedule.indexOf("/"))
            if (schedule != -1){
                ProjectList.add(schedule)
            }
        }
        // parsing to get all the branch names
        if (entryName =~ branch) {
            BranchList.add(entryName.substring(entryName.lastIndexOf(branch) + 9, entry.name.indexOf("_v") + 11))
        }
    }
    // leave unique branch names
    def uniqueBranchList = BranchList.unique()
    // leave unique project names
    def uniqueProjectList = ProjectList.unique()

    //reading configuration file
    def exclude = ""
    def exclude_temp = ""
    def BranchMap = [:]
    String delims = "[|]"
    def emptyList = []
    File conf = new File (conf_path)
    conf.eachLine {line, lineNumber->
        if (lineNumber == 1) {
            // set the exclude to have common exclude for all the branches
            exclude = line
            // store common exclude to reset exclude
            exclude_temp = line
        }
        // read the rest of the file
        else if (lineNumber > 2) {
            emptyList.add(Arrays.toString(line.split(delims)))
        }
    }


    // loop through unique project names
    def include = ""
    def destFile = ""
    def list = []
    uniqueProjectList.each{t->
        def project_temp = t - t.substring(t.lastIndexOf('_'), t.length())
        // loop through unique branch names
        uniqueBranchList.each{ i->
            // reset exclude to have just common exclude
            exclude = exclude_temp
            def branch_temp = i - i.substring (i.indexOf("_v"), i.indexOf("_r") + 3)
            // parse the list and add it to a map
            emptyList.eachWithIndex{ p, c ->
                // getting a project name
                BranchMap.put('project',(p.substring(0, p.indexOf(',')+1).replace(",","")).replace("[",""))
                p = p - p.substring(0, p.indexOf(',')+1)
                // getting a branch name
                BranchMap.branch = (p.substring(0, p.indexOf(',')+1).replace(",","")).replace(" ","")
                p = p - p.substring(0, p.indexOf(',')+1)
                // add a map to a list
                BranchMap.exclude = (p.substring (p.indexOf(', ')+2,p.indexOf(']')+1)).replace("]","")
                list[c] = BranchMap
                // check if there is configuration for that branch and that project in the properties file
                if ((list[c].project == project_temp)&&(branch_temp == list[c].branch)) {
                    // add the values that you want to exclude if the previos condition is true
                    exclude = exclude + ",**/" + (list[c].exclude).replace(",","/**,**/") + "/" + "**"
                }
            }
            // concatenate the folder name that needs to be included
            include = "**/" + t + "\\Branches\\" + i + "*/" + "**"
            // create the name of the destination file
            destFile = dir + t + "\\" + i + "_full.zip"
            // create zips
            def ant = new AntBuilder()
            ant.zip ( destfile: destFile ) {
                zipfileset (src:path, excludes:exclude, includes:include)
            }


        }
    }
}