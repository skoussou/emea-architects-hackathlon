
/**
 * Created by swinchester on 14/12/2016.
 */

import groovy.io.FileType

//def list = [[path:'/etc/santas-config/team.b.reindeer.1'],
//            [path:'/etc/santas-config/team.b.reindeer.2']]

def list = []
def reindeerList = []

def dir = new File("/etc/santas-config")
if (!dir.exists()){
    reindeerList.add("Prancer")
    reindeerList.add("Dancer")
}else{

    dir.eachFileRecurse (FileType.FILES) { file ->
        list << file
    }

    list.each { it ->
        println it.path
        if(it.path.contains('team.b')){
            String contents = new File(it.path).text
            if(!reindeerList.contains(contents)) {
                reindeerList.add(contents)
            }
        }
    }
}


return reindeerList