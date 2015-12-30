import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry
import java.nio.channels.FileChannel
import java.util.zip.ZipFile
import java.util.Enumeration

static void main (args){
	if (args.size() < 2) {
		println "directory source_file conf_file"
	}
	else {
	zip (args[0], args[1], args[2])
	}
}

static void zip ( String dir, String source_file, String conf_file  ){
    def ant = new AntBuilder()
    def path = dir + source_file
    def conf_path = dir + conf_file
    def branch = "Branches/"
    def project = "Projects/" 
    def schedule = ""
    def exclude = ""
    def include = ""
    def destFile = ""
    def version
    def emptyList = []
    ZipFile file = new ZipFile(path)
    def BranchList = []
    def ProjectList = []
    def list = []
    def exclude_temp = ""
    def include_temp = ""
    
    //reading zip file
    
    Enumeration <? extends ZipEntry> e = file.entries()                                                              
    while (e.hasMoreElements()) {
        ZipEntry entry = e.nextElement()
        def entryName = entry.getName()
        if (entryName =~ project) {  
            schedule = entryName.substring(entryName.lastIndexOf(project) + 9, entryName.lastIndexOf(project) + 20)       // parsing to get all the project names
            if (schedule != -1){
				ProjectList.add(schedule)
			}      
        } 
        if (entryName =~ branch) {  
            schedule = entryName.substring(entryName.lastIndexOf(branch) + 9, entry.name.indexOf("_v") + 2)            	  // parsing to get all the branch names 
            if (schedule != -1) {
                version = entryName.substring (entry.name.indexOf("_v")+ 1, entry.name.indexOf("_r") + 3)
                BranchList.add(schedule.replace ( "_v", "" ))           
            }      
        }  
    }
                                                                                                                    
    def uniqueBranchList = BranchList.unique()                                                                         	  // leave unique branch names
    def uniqueProjectList = ProjectList.unique()                                                                          // leave unique project names
        
        
    //reading configuration file   
         
    def BranchMap = [:]
    String delims = "[|]"
    File conf = new File (conf_path)
    conf.eachLine {line, lineNumber->
        if (lineNumber == 1) {           
            exclude = line                                                                                                // set the exclude to have common exclude for all the branches
            exclude_temp = line                                                                                           // store common exclude to reset exclude
        }
        else if (lineNumber > 2) {
			emptyList.add(Arrays.toString(line.split(delims)))													  	  	  // read the rest of the file
		}                                   
    }
    
    
    
    uniqueProjectList.each{t->                                                                                            // loop through unique project names
        new File(t).mkdirs()                                                                                              // create separate directory for each project name
        def temp = t - t.substring(t.lastIndexOf('_'), t.length())
        uniqueBranchList.each{ i->                                                                                        // loop through unique branch names
            emptyList.eachWithIndex{ p, c ->                                                                              // parse the list and add it to a map
                BranchMap.put('project',(p.substring(0, p.indexOf(',')+1).replace(",","")).replace("[",""))               // getting a project name
                p = p - p.substring(0, p.indexOf(',')+1)
                BranchMap.branch = (p.substring(0, p.indexOf(',')+1).replace(",","")).replace(" ","")                     // getting a branch name
                p = p - p.substring(0, p.indexOf(',')+1)
                BranchMap.exclude = (p.substring(0, p.indexOf(',')+1).replace(",","")).replace(" ","")                    // getting the name of the elements to exclude+
                p = p - p.substring(0, p.indexOf(',')+1)
                BranchMap.include = (p.substring (p.indexOf(', ')+2,p.indexOf(']')+1).replace(",","")).replace("]","")
                list[c] = BranchMap                                                                                       // add a map to a list
                destFile = dir + t + "\\" +i + ".zip"                                                                     // create the name of the destination file
                include = "**/" + t + "\\Branches\\" + i + "_v" + "*/" + "**"                                             // concatinate the folder name that needs to be included
                if ((list[c].project == temp)&&(i == list[c].branch)) {                                                   // check if there is configuration for that branch and that project in the properties file
                    exclude = exclude + ",**/" + (list[c].exclude).replace(",","/**,**/") + "/" + "**"                    // add the values that you want to exclude if the previos condition is true  
					println exclude
                    //include = include+",**/"+list[c].include+"/"+"**"
                }
                ant.zip ( destfile: destFile ) {
					zipfileset (src:path, excludes:exclude, includes:include)											  // create zips
				}                     									
                exclude = exclude_temp                                                                                    // reset exclude to have just common exclude
            }       
       }              
    }
}