#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<malloc.h>
#include<math.h>
#include<conio.h>
#include<jni.h>
#include<time.h>
#include"org_SocioNet_ServerRunner.h"


#define RELATION_FAMILY 3
#define RELATION_CLOSE_FRIEND 4
#define RELATION_FRIEND 5
#define USERNAME_LEN 50
#define GENDER_LEN 1
#define Hash_TABLE_SIZE 200
#define ARRAY_SIZE 200
#define NO_OF_DIGITS_USER_ID 6
#define NAME_LEN 20
#define COLLEGE_LEN 100
#define PASSWORD_LEN 10
#define STATUS_LEN 100
#define CITY_LEN 20
#define PENDINGREQUESTSIZE 20
#define MESSAGESIZE 30
#define MAXMESSAGECOUNT 20
#define IMAGEPATHLENGTH 255
#define BIGARRAYSIZE 1000
typedef enum status_type{FAILURE,SUCCESS} status_code;
#define MEMBERSCOUNTSIZE 10//---------------------------------------------------
#define POSTSIZE 255//----------------------------------------------------------
#define GROUPNAMESIZE 20//----------------------------------------------------------
#define MAXNUMBEROFGROUPS 20
typedef struct members_tag
{
	int membersID[MEMBERSCOUNTSIZE];
	int count;
	struct members_tag* next;
}members;//-----------------------------------------------------------------------------------------------------------------------------------

typedef struct posts_tag
{
	int postUserID;
	char postContent[POSTSIZE];
	long int timestamp;
	struct posts_tag* nextPost;
}post;//-----------------------------------------------------------------------------------------------------------------------------------


typedef struct admin_pending_request_tag
{
	int membersID[MEMBERSCOUNTSIZE];
	int groupID;
	int count;
	struct admin_pending_request_tag* next;
}adminPendingRequests;//-----------------------------------------------------------------------------------------------------------------------------------


typedef struct group_tag
{
	int groupID;
	int adminID;
	char groupName[GROUPNAMESIZE];
	members* groupMembers;
	post* groupPosts;
	adminPendingRequests* groupPendingRequests;
	struct group_tag* nextGroup;
}group;//-----------------

typedef struct node_type
{
    int data;
    struct node_type *next;
}Node;

typedef struct Queue_tag
{
    Node* front;
    Node* rear;
}Queue;

typedef struct message_tag
      {
            int FromUserID;
            char MessageBody[MESSAGESIZE];
            bool IsRead;
			int messageID;
            struct message_tag* NextMessage;
      }Message;

typedef struct user_data_tag
{
    char Name[NAME_LEN];
    char Password[PASSWORD_LEN];
    char City[CITY_LEN];
    int Age;
    char Gender;
    char College[COLLEGE_LEN];
} user_data;

typedef struct pending_friends_status
{
    int PendingFriendRequest[PENDINGREQUESTSIZE];
    char Status[STATUS_LEN];
}PendingFriendsStatus;

typedef struct weight_tag
{
    int relation;
} weights;

typedef struct edge_tag
{
    struct vertex_tag * vertex_point;
    struct weight_tag weight;
    struct edge_tag * next_edge;
} edge;

typedef struct vertex_tag
{
    int UserId;
    struct user_data_tag data;
    struct edge_tag * first_edge;
    struct vertex_tag * next_vertex;
    struct pending_friends_status* PendingFriendsAndStatus;
	struct message_tag* UserMessages;
	char ImagePath[IMAGEPATHLENGTH];
	long int prevLoggedIn;
	long int lastLoggedIn;
	long int profilePicTimestamp;
	long int statusTimestamp;
	int memberOfGroups[MAXNUMBEROFGROUPS];
} vertex;

typedef struct graph_tag
{
    int no_vertices;
    int LastAddedUserID;
	int lastAddedGroupID;
	group* groups;
    struct vertex_tag * vertex_start;
} graph;

typedef struct table_tag
{
    int key;
    vertex* VertexPointer;
    struct table_tag* next;
}Table;

typedef struct hash_table_tag
{
    int size;
    Table* table[Hash_TABLE_SIZE];
}HashTable;

typedef struct List_tag
{
	char city_name[CITY_LEN];
	vertex* vptr;
	struct List_tag* next;
	vertex* last_vertex;
}city_list;

typedef struct alphaArray_tag
{
	char alphabet;
	city_list* list;
}alphaArray;

typedef struct group_table_tag//-----------------------------------------------------------------------------------------------------------------------------------
{
    int key;
    group* groupPointer;
    struct group_table_tag* next;
}groupTable;

typedef struct group_hash_table_tag//-----------------------------------------------------------------------------------------------------------------------------------
{
    int size;
    groupTable* table[Hash_TABLE_SIZE];
}groupHashTable;


static alphaArray cityAlpha[26];

void deleteVertexStructure(vertex *v);
groupHashTable* ght=NULL;
int IsFriend(int UserID1,int UserID2);
/*add*/vertex* addVertex(char Name[USERNAME_LEN],int UserId,char Password[PASSWORD_LEN],char city[CITY_LEN],int age,char gender,char college[COLLEGE_LEN],char ImagePath[IMAGEPATHLENGTH],long int profilePicTimestamp,long int lastLoggedIn,long int statusTimeStamp);
graph * create_graph();
char path[100]="F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB";
/*add*/void addEdge(int userId1,int userId2,int relation);
int user_exist(int userId);
void print_graph();
void deleteVertex(int userID);
void read_create_links();
void file_loadUsers();
void file_saveUsers();
void deleteEdge(int user1,int user2);
void file_save_links();
void AddNewPair(Table* NewPair);
Table* CreateNewPair(int key,vertex* VertexPointer);
void CreateHashTable(int size);
int HashFunction(int key);
void ViewHashTable();
void DeleteHashTableEntry(int UserId);
static graph* Graph=NULL;
HashTable* ht=NULL;
vertex* SearchUser(int UserId);
/*added*/void PrintFriendList(int user1);
/*add*/void PrintMutualFriends(int UserID1,int UserID2,char RetArray[]);
/*add*/int AuthenticateUser(int UserID,char password[],char RetArr[]);
/*add*/char* LoadProfile(int UserID);
/*add*/char* SearchAnyUser(int UserID,char UserName[NAME_LEN]);
char* C_LoadProfile(int UserID);
void file_save_pend_status();
void CopyPendingRequestsAndStatus(int UserID,int PendingRequest[PENDINGREQUESTSIZE],char Status[STATUS_LEN]);
int IsFriend(int UserID1,int UserID2);
void ReadPendingRequestAndStatus();
void SendFriendRequest(int FromUserID,int ToUserID);
void acceptRejectRequest(int UserId1,int UserId2,int accept);
int IsPendingRequestBetweenUsers(int FromUserID,int ToUserID);
/*add*/char* UpdateCityDetails(int UserID,char city[CITY_LEN]);
/*add*/char* UpdatePassword(int UserID,char password[PASSWORD_LEN]);
/*add*/char* UpdateCollegeDetails(int UserID,char college[COLLEGE_LEN]);
/*add*/char* SendNotification(int UserID);
void AddMessageToUser(int vertexno,Message* lptr);
Message* AddMessageToList(Message* lptr,Message* MessageNode);
void readMessagesFromFile();
void WriteMessagesToFile();
/*add*/char* GetAllMessages(int UserID);
void Initialize_Queue(Queue *qptr);
int isEmptyQueue(Queue *qptr);
status_code Insert_Queue(Queue *qptr,int d);
status_code Delete_Queue(Queue *qptr,int *dptr);
void SendMessageToUser(int FromUserID,int ToUserID,char MessageBody[MESSAGESIZE]);
/*add*/JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_breadthFirst
  (JNIEnv *, jclass, jint, jstring);
void Visit(vertex *v);
void addToResult(char retArr[1000],int UserId,vertex *v);
int giveSearchConstraint();
int getUserId();
/*add*/int countMutualFriend(int UserID1,int UserID2);
/*add*/void updateProfilePic(int UserID,char Path[]);
int getNoOfVertices();
int* getEdges(int UserID);
int getUserId(vertex *v);
void insertVertex(vertex *v);
int convertAlphaToInt(char c);
city_list* createCity(char city[]);
void createLastLink(vertex *v,char city[],int pos,int deletion);
void createNextLink(vertex *v,char city[],int pos);
void initialise_alphaArray();
void readGroups();
void addGroupsToGraph(group* groupList);;
void printGroups();
void writeGroupsToFile();
group* searchGroup(int groupID);
void createGroupHashTable(int size);
groupTable* createNewGroupPair(int key,group* groupPointer);
void addNewGroupPair(groupTable* NewPair);
char* createNewGroupJNI(int adminID,char groupName[]);
char* acceptOrRejectGroupRequest(int groupID,int fromUserID,int ofUserID,int accept);
char* addMemberToGroup(int groupID,int userID);
char* getAllPosts(int groupID);
char* getGroupInfo(int groupID);
char* generateNewsfeed(int userID);
char* getPerPersonGroupList(int userID);
int lastUserId=0;

JNIEXPORT void JNICALL Java_org_SocioNet_ServerRunner_mainCall
  (JNIEnv *, jclass)
{
	FILE *fp = fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/abhb.txt","w");
    initialise_alphaArray();
	fprintf(fp,"here main call\n");
	Graph=create_graph();
	if(Graph==NULL)
		fprintf(fp,"graph is null\n");
    fprintf(fp,"here main call\n");
	file_loadUsers();
	fprintf(fp,"here main call\n");
    read_create_links();
    fprintf(fp,"here main call\n");
    ReadPendingRequestAndStatus();
    readGroups();
    fprintf(fp,"here main call\n");
	readMessagesFromFile();
	fprintf(fp,"here main call\n");
	fclose(fp);
}

graph * create_graph()
{
    graph * Graph= (graph *) malloc(sizeof(graph));
    Graph->no_vertices=0;
    Graph->vertex_start=NULL;
    return Graph;
}

vertex* addVertex(char Name[USERNAME_LEN],int UserId,char Password[PASSWORD_LEN],char city[CITY_LEN],int age,char gender,char college[COLLEGE_LEN],char imagePath[],long int profilePicTimestamp,long int lastLoggedIn,long int statusTimeStamp)
{
    vertex * tmp_vertex=NULL;
    int exist=0;
    vertex * new_vertex=(vertex *) malloc(sizeof(vertex));
    PendingFriendsStatus* NewPendingFriendsStatus=(PendingFriendsStatus*)malloc(sizeof(PendingFriendsStatus));
    new_vertex->first_edge=NULL;
    new_vertex->next_vertex=NULL;
    new_vertex->PendingFriendsAndStatus=NULL;
    new_vertex->data.Name[0]='\0';
    new_vertex->data.College[0]='\0';
    new_vertex->data.City[0]='\0';
	new_vertex->ImagePath[0]='\0';
    new_vertex->UserMessages=NULL;
	new_vertex->profilePicTimestamp=profilePicTimestamp;
	new_vertex->lastLoggedIn=lastLoggedIn;
	new_vertex->prevLoggedIn=lastLoggedIn;
	new_vertex->statusTimestamp=statusTimeStamp;
    if(UserId==-1)
    {
        UserId=Graph->LastAddedUserID+1;
    }
    new_vertex->UserId=UserId;
    new_vertex->data.Age=age;
    new_vertex->data.Gender=gender;
    new_vertex->PendingFriendsAndStatus=NewPendingFriendsStatus;
    new_vertex->PendingFriendsAndStatus->PendingFriendRequest[0]=-1;
    new_vertex->PendingFriendsAndStatus->Status[0]='\0';
    strcpy(new_vertex->data.Name,Name);
    strcpy(new_vertex->data.Password,Password);
    strcpy(new_vertex->data.City,city);
    strcpy(new_vertex->data.College,college);
	strcpy(new_vertex->ImagePath,imagePath);
    tmp_vertex=Graph->vertex_start;
    if(tmp_vertex==NULL)
    {
		insertVertex(new_vertex);
        Graph->no_vertices++;
        if(lastUserId<UserId)
		{
			Graph->LastAddedUserID=UserId;
			lastUserId=UserId;
		}
    }
    else
    {

        while(exist ==0 && tmp_vertex->next_vertex !=NULL)
        {
            if(tmp_vertex->UserId == UserId)
            {
                exist=1;
            }
            tmp_vertex=tmp_vertex->next_vertex;
        }
        if(exist==0 && tmp_vertex !=NULL)
        {
			insertVertex(new_vertex);
            Graph->no_vertices++;
            if(lastUserId<UserId)
			{
				Graph->LastAddedUserID=UserId;
				lastUserId=UserId;
			}
        }
    }
    return new_vertex;
}


JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_AddVertex
  (JNIEnv * env, jclass, jstring jName, jint UserId, jstring jPassword, jint age, jchar jGender, jstring jCity, jstring jCollege)
{
	jboolean boolean;
	const char* Name=(env)->GetStringUTFChars(jName,&boolean);
	const char* Password=(env)->GetStringUTFChars(jPassword,&boolean);
	const char* city=(env)->GetStringUTFChars(jCity,&boolean);
	const char* college=(env)->GetStringUTFChars(jCollege,&boolean);
	const char gender=jGender;
    vertex * tmp_vertex=NULL;
    int exist=0;
    vertex * new_vertex=(vertex *) malloc(sizeof(vertex));
    PendingFriendsStatus* NewPendingFriendsStatus=(PendingFriendsStatus*)malloc(sizeof(PendingFriendsStatus));
    new_vertex->data.Name[0]='\0';
    new_vertex->data.College[0]='\0';
    new_vertex->data.City[0]='\0';
	new_vertex->ImagePath[0]='\0';
    if(UserId==-1)
    {
        UserId=Graph->LastAddedUserID+1;
    }
    new_vertex->first_edge=NULL;
    new_vertex->next_vertex=NULL;
    new_vertex->PendingFriendsAndStatus=NULL;
	new_vertex->UserMessages=NULL;
    new_vertex->UserId=UserId;
    new_vertex->data.Age=age;
    new_vertex->data.Gender=gender;
    new_vertex->PendingFriendsAndStatus=NewPendingFriendsStatus;
    new_vertex->PendingFriendsAndStatus->PendingFriendRequest[0]=-1;
    new_vertex->PendingFriendsAndStatus->Status[0]='\0';
    strcpy(new_vertex->data.Name,Name);
    strcpy(new_vertex->data.Password,Password);
    strcpy(new_vertex->data.City,city);
	strcpy(new_vertex->ImagePath,"NOT");
    strcpy(new_vertex->data.College,college);
    tmp_vertex=Graph->vertex_start;
    if(tmp_vertex==NULL)
    {
		insertVertex(new_vertex);
        Graph->no_vertices++;
        Graph->LastAddedUserID=UserId;
    }
    else
    {
        while(exist ==0 && tmp_vertex->next_vertex !=NULL)
        {
            if(tmp_vertex->UserId == UserId)
            {
                exist=1;
            }
			else
			{
				tmp_vertex=tmp_vertex->next_vertex;
			}
        }
        if(exist==0 && tmp_vertex !=NULL)
        {
			insertVertex(new_vertex);
            Graph->no_vertices++;
            Graph->LastAddedUserID=UserId;
        }
    }
    char RetArr[30],temp[5];
	RetArr[0]='\0';
	strcat(RetArr,"SIGNEDUP");
	strcat(RetArr,":");
	strcat(RetArr,itoa(new_vertex->UserId,temp,10));
	strcat(RetArr,":");
	strcat(RetArr,new_vertex->data.Name);
	printf("%s is added vertex\n",RetArr);
	return (env)->NewStringUTF(RetArr);
}

void insertVertex(vertex *v)
{
	Table* NewPair=CreateNewPair(v->UserId,v);
	AddNewPair(NewPair);
	char city[CITY_LEN]="";
	char alpha;
	int posInAlphaArr=0,flag=0;
	strcpy(city,v->data.City);
	alpha=city[0];
	city_list *curr,*prev,*temp;
	vertex *curr_vertex,*prev_vertex;
	prev_vertex=NULL;
	curr_vertex=NULL;
	temp=NULL;
	prev=NULL;
	curr=NULL;
	posInAlphaArr=convertAlphaToInt(alpha);
	if(cityAlpha[posInAlphaArr].list==NULL)
	{
		cityAlpha[posInAlphaArr].list=createCity(city);
		cityAlpha[posInAlphaArr].list->vptr=v;
		cityAlpha[posInAlphaArr].list->last_vertex=v;
		createLastLink(v,city,posInAlphaArr,0);
		createNextLink(v,city,posInAlphaArr);
	}
	else
	{
		curr=cityAlpha[posInAlphaArr].list;
		while(flag==0 && curr!=NULL && strcmp(curr->city_name,city)<=0)
		{
			if(strstr(curr->city_name,city)!=NULL)
				flag=1;
			else
			{
				prev=curr;
				curr=curr->next;
			}
		}
		if(flag==0)
		{
			temp=createCity(city);
			if(prev==NULL)
			{
				cityAlpha[posInAlphaArr].list=temp;
				temp->next=curr;
			}
			else if(prev!=NULL)
			{
				prev->next=temp;
				temp->next=curr;
			}
			temp->vptr=v;
			temp->last_vertex=v;
			createLastLink(v,city,posInAlphaArr,0);
			createNextLink(v,city,posInAlphaArr);
		}
		else
		{
			if(curr!=NULL)
			{
				curr_vertex=curr->vptr->next_vertex;
				if(curr_vertex==NULL)
				{
					curr->last_vertex=v;
				}
				v->next_vertex=curr_vertex;
				curr->vptr->next_vertex=v;
			}
		}
	}
}

void deleteEdge(int user1,int user2)///Ambarish
{
    vertex* vertex1=Graph->vertex_start;
    vertex* vertex2=Graph->vertex_start;
    vertex1=SearchUser(user1);
    vertex2=SearchUser(user2);
    if(vertex1!=NULL && vertex2!=NULL)
    {
        if(vertex1->first_edge!=NULL)
        {
            edge* edge_to_delete1=vertex1->first_edge;
            edge* edge_to_delete2=vertex2->first_edge;
            edge* prev1=NULL;
            edge* prev2=NULL;
            int count1=0,count2=0;
            while((edge_to_delete1->vertex_point)->UserId!=user2 &&edge_to_delete1!=NULL)
            {
                prev1=edge_to_delete1;
                edge_to_delete1=edge_to_delete1->next_edge;
                count1++;
            }
            if(prev1!=NULL)
            {
                prev1->next_edge=edge_to_delete1->next_edge;
                free(edge_to_delete1);
            }
            else
            {
                if(count1==0)
                {
                    vertex1->first_edge=vertex1->first_edge->next_edge;
                    free(edge_to_delete1);
                }
                else
                {
                    printf("You are not connected with that user!!!\n");
                }
            }


            while((edge_to_delete2->vertex_point)->UserId!=user1 && edge_to_delete2!=NULL)
            {
                prev2=edge_to_delete2;
                edge_to_delete2=edge_to_delete2->next_edge;
                count2++;
            }
            if(prev2!=NULL)
            {
                prev2->next_edge=edge_to_delete2->next_edge;
                free(edge_to_delete2);
            }
            else
            {
                if(count2==0)
                {
                    vertex2->first_edge=vertex2->first_edge->next_edge;
                    free(edge_to_delete2);
                }
                else
                {
                    printf("You are not connected with that user!!!\n");
                }
            }
        }
    }
}

JNIEXPORT void JNICALL Java_org_SocioNet_ServerRunner_DeleteVertex
  (JNIEnv *, jclass, jint UserId)///Ambarish
{

    vertex* vertex2Del;
    vertex* tmp=NULL;
    edge* tmpEdge=NULL,*tmpEdge2=NULL,*prevEdge2=NULL;
    int found=0;
	vertex2Del=SearchUser(UserId);
    tmpEdge=vertex2Del->first_edge;
    while(tmpEdge!=NULL)
    {
        tmp=tmpEdge->vertex_point;
        found=0;
        prevEdge2=NULL;
        tmpEdge2=tmp->first_edge;
        while(found==0)
        {
            if(tmpEdge2->vertex_point==vertex2Del)
            {
                found=1;
                if(prevEdge2==NULL)
                {
                    tmp->first_edge=tmpEdge2->next_edge;
                }
                else
                {
                    prevEdge2->next_edge=tmpEdge2->next_edge;
                }
                free(tmpEdge2);
            }
            else
            {
                prevEdge2=tmpEdge2;
                tmpEdge2=tmpEdge2->next_edge;
            }
        }
        vertex2Del->first_edge=tmpEdge->next_edge;
        free(tmpEdge);
		tmpEdge=vertex2Del->first_edge;
    }
	DeleteHashTableEntry(UserId);
    deleteVertexStructure(vertex2Del);
}


void addEdge(int userId1,int userId2,int relation)
{
    //search for userId1 and UserId2
    vertex * tmp_vertex=Graph->vertex_start;
    vertex * user1 =NULL,* user2 =NULL;
    user1=SearchUser(userId1);
    user2=SearchUser(userId2);
    if(user1 !=NULL && user2 !=NULL)
    {
        edge* user1_new_edge=(edge *) malloc(sizeof(edge));
        edge* user2_new_edge=(edge *) malloc(sizeof(edge));
        edge* tmp_edge=NULL;

        user1_new_edge->next_edge=NULL;
        user2_new_edge->next_edge=NULL;

        user1_new_edge->vertex_point=user2;
        user2_new_edge->vertex_point=user1;

        user1_new_edge->weight.relation=relation;
        user2_new_edge->weight.relation=relation;

        tmp_edge=user1->first_edge;
        int edge_exist=0;
        if(tmp_edge !=NULL)
        {
            while(edge_exist==0 && tmp_edge ->next_edge !=NULL)
            {
                if(tmp_edge->vertex_point==user2)
                {
                    edge_exist=1;
                }
                tmp_edge=tmp_edge->next_edge;
            }
            if(tmp_edge->vertex_point==user2)
            {
                edge_exist=1;
            }
            if(edge_exist==0) tmp_edge->next_edge=user1_new_edge;
        }
        else
        {
            user1->first_edge=user1_new_edge;
        }
        if(edge_exist==0)
        {
            tmp_edge=user2->first_edge;
            if(tmp_edge !=NULL)
            {
                while(tmp_edge ->next_edge !=NULL)
                {
                    tmp_edge=tmp_edge->next_edge;
                }
                tmp_edge->next_edge=user2_new_edge;
            }
            else
            {
                user2->first_edge=user2_new_edge;
            }
        }
    }
}


int user_exist(int userId)
{
    vertex * tmp_vertex=Graph->vertex_start;
    while(tmp_vertex!=NULL && tmp_vertex->next_vertex !=NULL)
    {
        if(tmp_vertex->UserId == userId)
        {
            return 1;
        }
        tmp_vertex=tmp_vertex->next_vertex;
    }
    return 0;
}

vertex* SearchUser(int UserId)
{
    Table* SearchingTable=ht->table[HashFunction(UserId)];
    vertex* retval=NULL;
    if(SearchingTable!=NULL)
    {
        while(retval==0 && SearchingTable!=NULL)
        {
            if(SearchingTable->key==UserId)
            {
                retval=SearchingTable->VertexPointer;
            }
            else
            {
                SearchingTable=SearchingTable->next;
            }
        }
    }
    return retval;
}

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_printGraph
  (JNIEnv * env, jclass)
{
    vertex * tmp_vertex=NULL;
    edge * tmp_edge=NULL;
	//jcharArray charArr = (env)->NewCharArray(200);
	char retArr[200];
	retArr[0]='\0';
	strcat(retArr,"</br></br>");
	char temp[3];
	//jcharArray charArr = (jcharArray)(env)->NewCharArray(env,200);
	int i=0;
    tmp_vertex=Graph->vertex_start;
    while(tmp_vertex!=NULL && tmp_vertex!=NULL)
    {
        //printf("%d|||%s===>",tmp_vertex->UserId,tmp_vertex->data.Name);
		strcat(retArr,tmp_vertex->data.Name);
        strcat(retArr,"=>");
		tmp_edge=tmp_vertex->first_edge;
        while(tmp_edge!=NULL)
        {
            //printf("%d :",tmp_edge->vertex_point->UserId);
			//strcat(retArr,itoa(tmp_edge->vertex_point->UserId,temp,10));
			strcat(retArr,tmp_edge->vertex_point->data.Name);
			strcat(retArr,"|");
            tmp_edge=tmp_edge->next_edge;
        }
        //printf("\n");
		strcat(retArr,"</br>");
        tmp_vertex=tmp_vertex->next_vertex;
    }
	return (env)->NewStringUTF(retArr);
}

JNIEXPORT void JNICALL Java_org_SocioNet_ServerRunner_SaveUsersToFile
  (JNIEnv *, jclass)
{
	printf("at entry\n");
    FILE * fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/users.db","w");
    vertex *tmp_vertex=NULL;
    tmp_vertex=Graph->vertex_start;

    if(fp!=NULL && tmp_vertex!=NULL)
    {
        while(tmp_vertex!=NULL)
        {
            fprintf(fp,"%d[%s,%s,%s,%s,%d,%c,%s,%ld,%ld]\n",tmp_vertex->UserId,tmp_vertex->data.Name,tmp_vertex->data.Password,tmp_vertex->data.City,tmp_vertex->data.College,tmp_vertex->data.Age,tmp_vertex->data.Gender,tmp_vertex->ImagePath,tmp_vertex->profilePicTimestamp,tmp_vertex->lastLoggedIn);
            tmp_vertex=tmp_vertex->next_vertex;
        }
        fclose(fp);
        //return SUCCESS;
    }
	printf("at exit\n");
}


JNIEXPORT void JNICALL Java_org_SocioNet_ServerRunner_SaveLinksToFile
  (JNIEnv *, jclass)
{
    vertex * tmp_vertex=NULL;
    edge * tmp_edge=NULL;
    FILE * fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/link.db","w");

    tmp_vertex=Graph->vertex_start;
    while(fp!=NULL && tmp_vertex!=NULL && tmp_vertex!=NULL)
    {
        fprintf(fp,"%d[",tmp_vertex->UserId);
        tmp_edge=tmp_vertex->first_edge;
        while(tmp_edge!=NULL)
        {
            fprintf(fp,"%d",tmp_edge->vertex_point->UserId);
            if(tmp_edge->next_edge !=NULL) fprintf(fp,",");
            tmp_edge=tmp_edge->next_edge;
        }
        fprintf(fp,"]\n");
        tmp_vertex=tmp_vertex->next_vertex;
    }
    fclose(fp);
}

void file_loadUsers()
{
    FILE *fp;

    char c,name[USERNAME_LEN],userId_tmp[20],age_tmp[4],password[PASSWORD_LEN],city[CITY_LEN],college[COLLEGE_LEN],ImagePath[IMAGEPATHLENGTH],timestamp[20];
    char gender;
    int userId=0,age=0,i;
	long int profilePicTimestamp,lastLoggedIn,statusTimeStamp;
    strcpy(name,"");
	ImagePath[0]='\0';
    fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/users.db","r");
    CreateHashTable(Hash_TABLE_SIZE);
    if(fp!=NULL)
    {
        c=getc(fp);
        while(c!=EOF)
        {
            i=0;
            while(c!='[')
            {
                userId_tmp[i]=c;
                i++;
                c=getc(fp);
            }
            userId_tmp[i]='\0';
            //if(strcmp(roll_temp,"")==0) flag_roll=0;
            userId=atoi(userId_tmp);
            //printf("%d",roll);

            c=getc(fp);
            i=0;
            while(c!=',')
            {
                name[i]=c;
                i++;
                c=getc(fp);
            }
            name[i]='\0';

            c=getc(fp);
            i=0;
            while(c!=',')
            {
                password[i]=c;
                i++;
                c=getc(fp);
            }
            password[i]='\0';

            c=getc(fp);
            i=0;
            while(c!=',')
            {
                city[i]=c;
                i++;
                c=getc(fp);
            }
            city[i]='\0';

            c=getc(fp);
            i=0;
            while(c!=',')
            {
                college[i]=c;
                i++;
                c=getc(fp);
            }
            college[i]='\0';

            c=getc(fp);
            i=0;
            while(c!=',')
            {
                age_tmp[i]=c;
                i++;
                c=getc(fp);
            }
            age_tmp[i]='\0';
            age=atoi(age_tmp);

            c=getc(fp);
            i=0;
            gender=c;
            c=getc(fp);
			if(c==',')
			{
				c=getc(fp);
			}
			while(c!=',')
			{
				ImagePath[i]=c;
				i++;
				c=getc(fp);
			}
			ImagePath[i]='\0';
			i=0;
			c=getc(fp);
				i=0;
			while(c!=',')
			{
				timestamp[i]=c;
				i++;
				c=getc(fp);
			}
			timestamp[i]='\0';
			profilePicTimestamp=atol(timestamp);
			i=0;
			c=getc(fp);
			while(c!=',')
			{
				timestamp[i]=c;
				i++;
				c=getc(fp);
			}
			timestamp[i]='\0';
			lastLoggedIn=atol(timestamp);
			i=0;
			c=getc(fp);
			while(c!=']')
			{
				timestamp[i]=c;
				i++;
				c=getc(fp);
			}
			timestamp[i]='\0';
			statusTimeStamp=atol(timestamp);
			i=0;
			c=getc(fp);
            if(c=='\n')
            {
                c=getc(fp);
            }
			

            if(!user_exist(userId))
                {
					vertex* new_vertex=addVertex(name,userId,password,city,age,gender,college,ImagePath,profilePicTimestamp,lastLoggedIn,statusTimeStamp);
					//Table* NewPair=CreateNewPair(userId,new_vertex);
                    //AddNewPair(NewPair);
                }
        }
        fclose(fp);
    }
}



void read_create_links()
{
    FILE * fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/link.db","r");
    int i=0,vertex,linked_vertex;
    char c,tmp_vertex[4],tmp_linked_vertex[4];
    if(fp!=NULL)
    {
        c=getc(fp);
        while(c!=EOF)
        {
            i=0;
            while(c!='[')
            {
                tmp_vertex[i]=c;
                i++;
                c=getc(fp);
            }
            tmp_vertex[i]='\0';
            vertex=atoi(tmp_vertex);

            c=getc(fp);
            while(c!=']')
            {
                i=0;
                while(c!=']' && c!=',')
                {
                    tmp_linked_vertex[i]=c;
                    i++;
                    c=getc(fp);
                }
                tmp_linked_vertex[i]='\0';
                linked_vertex=atoi(tmp_linked_vertex);
                if(linked_vertex > vertex)
                {
                    //add edge
                    addEdge(vertex,linked_vertex,3);
                }
                if(c==',') c=getc(fp);
            }
            c=getc(fp);
            if(c=='\n')
            {
                c=getc(fp);
            }
        }
    }
    fclose(fp);
}

//-------------------------------HashTable--------------------------------------
void CreateHashTable(int size)
{
    //HashTable* ht=NULL;
    if(size>=1)
    {
        ht=(HashTable*)malloc(sizeof(HashTable));
        ht->size=size;
        //ht->table=(Table*)malloc((sizeof(Table*)*100));
        int i=0;
        for(i=0;i<ht->size;i++)
        {
            ht->table[i]=NULL;
        }
    }
}

Table* CreateNewPair(int key,vertex* VertexPointer)
{
    Table* NewPair=NULL;
    NewPair=(Table*)malloc(sizeof(Table));
    NewPair->key=key;
    NewPair->VertexPointer=VertexPointer;
    NewPair->next=NULL;
    return NewPair;
}

void AddNewPair(Table* NewPair)
{
    if(ht!=NULL)
    {
        Table* AddingTable=NULL;
        int k=HashFunction(NewPair->key);
        AddingTable=(ht->table[k]);
        if(AddingTable==NULL)
        {
            ht->table[k]=NewPair;
            //printf("%d is in hashtable\n",ht->table[NewPair->key]);
        }
        else
        {
            while(AddingTable->next!=NULL)
            {
                //printf("%d is in hashtable\n",AddingTable->key);
                AddingTable=AddingTable->next;
            }
            AddingTable->next=NewPair;
        }
    }
    else
    {
        printf("problem!!!!");
    }
}

int HashFunction(int key)
{
    return (key % Hash_TABLE_SIZE);
}

void ViewHashTable()
{
    int i=0;
    while(i<ht->size)
    {
        Table* TableIterator=NULL;
        TableIterator=ht->table[i];
        while(TableIterator!=NULL)
        {
            printf("%s\t",(TableIterator->VertexPointer)->data.Name);
            TableIterator=TableIterator->next;
        }
        i++;
        printf("\n");
    }
}

void DeleteHashTableEntry(int UserId)
{
    Table* TableToBeDeleted=ht->table[HashFunction(UserId)];
    if(TableToBeDeleted->key==UserId)
    {
        Table* prev=TableToBeDeleted;
        TableToBeDeleted=TableToBeDeleted->next;
        free(prev);
    }
    else
    {
        Table* prev=TableToBeDeleted;
        while(TableToBeDeleted->key!=UserId && TableToBeDeleted!=NULL)
        {
            prev=TableToBeDeleted;
            TableToBeDeleted=TableToBeDeleted->next;
        }
        prev->next=TableToBeDeleted->next;
        free(TableToBeDeleted);
    }
}

//JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_PrintFriendList
//  (JNIEnv *env, jclass, jint user1)
JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_PrintFriendList
  (JNIEnv * env, jclass, jint user1)
{
	FILE *fp1 = fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/abhb.txt","a");
	FILE *fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/Log.txt","w");
	fprintf(fp,"IN frined List\n");
	printf("print friendlist\n");
	vertex* CurrentVertex=NULL;
    CurrentVertex=SearchUser(user1);
    printf("\n\n\n hello %d\n",CurrentVertex);
	char retArr[200*5];
	retArr[0]='\0';
    char temp[NO_OF_DIGITS_USER_ID];
	if(CurrentVertex!=NULL)
	{
		edge* CurrentEdge=CurrentVertex->first_edge;
		if(CurrentEdge==NULL)
		{
			retArr[0]='\n';
			retArr[1]='\0';
		}
		else
		{
			while(CurrentEdge!=NULL)
			{
				strcat(retArr,CurrentEdge->vertex_point->data.Name);
				strcat(retArr,":");
				strcat(retArr,itoa(CurrentEdge->vertex_point->UserId,temp,10));
				strcat(retArr,":");
				strcat(retArr,(CurrentEdge->vertex_point)->ImagePath);
				if(CurrentEdge->next_edge!=NULL)
				{
					strcat(retArr,"\n");
				}
				CurrentEdge=CurrentEdge->next_edge;
			}
		}
    }
	fprintf(fp1,"%s",retArr);
	fclose(fp1);
	fclose(fp);
	return (env)->NewStringUTF(retArr);
}

void PrintMutualFriends(int UserID1,int UserID2,char RetArray[])
{
    vertex* User1=NULL;
    vertex* User2=NULL;
    User1=SearchUser(UserID1);
    User2=SearchUser(UserID2);
    edge* EdgeIterator1=User1->first_edge;
    edge* EdgeIterator2=User2->first_edge;
    char temp[NO_OF_DIGITS_USER_ID];
    while(EdgeIterator1!=NULL)
    {
        EdgeIterator2=User2->first_edge;
        while(EdgeIterator2!=NULL)
        {
            if(EdgeIterator2->vertex_point==EdgeIterator1->vertex_point)
            {
                strcat(RetArray,EdgeIterator2->vertex_point->data.Name);
                strcat(RetArray,":");
                strcat(RetArray,itoa(EdgeIterator2->vertex_point->UserId,temp,10));
                strcat(RetArray,"\n");
                break;
            }
            EdgeIterator2=EdgeIterator2->next_edge;
        }
        EdgeIterator1=EdgeIterator1->next_edge;
    }
    //printf("Mutual Friend:\t%s",RetArray);
}

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_AuthenticateUser
  (JNIEnv * env, jclass, jint UserID, jstring jpassword)
{
    jboolean boolean;
    const char* password;
    password=(env)->GetStringUTFChars(jpassword,&boolean);
    char RetArr[20];
    vertex* User=NULL;
    User=SearchUser(UserID);
    RetArr[0]='\0';
    char temp[5];
	long int t;
    if(User==NULL)
    {
        strcat(RetArr,"USERDOESNOTEXIST:");
        return (env)->NewStringUTF(RetArr);
    }
    else
    {
                if(strcmp(User->data.Password,password)==0)
                {
					t=(long int)time(0);
					User->prevLoggedIn=User->lastLoggedIn;
					User->lastLoggedIn=t;
                    strcat(RetArr,"SIGNEDIN:");
                    strcat(RetArr,User->data.Name);
                    strcat(RetArr,":");
                    strcat(RetArr,itoa(UserID,temp,10));
					strcat(RetArr,"\n");
                    return (env)->NewStringUTF(RetArr);
                }
                else
                {
                    strcat(RetArr,"WRONGPASSWORD:");
                    return (env)->NewStringUTF(RetArr);
                }
    }

}

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_SearchAnyUser
  (JNIEnv *env, jclass, jint UserID, jstring JavaUserName)

{
    const char* UserName;
    jboolean boolean;
    UserName=(env)->GetStringUTFChars(JavaUserName,&boolean);
    vertex* CurrentVertex=NULL;
    CurrentVertex=Graph->vertex_start;
    printf("UserID:%d",UserID);
    char RetArr[ARRAY_SIZE*5],temp[NO_OF_DIGITS_USER_ID],MutualFriends[ARRAY_SIZE];
    RetArr[0]='\0';
    int flag,i;
    vertex* User=SearchUser(UserID);
    while(CurrentVertex!=NULL)
    {
        if(CurrentVertex->data.Name[0]==UserName[0] || CurrentVertex->data.Name[0]==(UserName[0]-32) && CurrentVertex->UserId!=UserID)
        {
            i=1;
            flag=1;
            while(flag==1 && CurrentVertex->data.Name[i]!='\0' && UserName[i]!='\0')
            {
                if(CurrentVertex->data.Name[i]==UserName[i])
                {
                    flag=1;
                    i++;
                }
                else
                {
                    flag=0;
                }
            }
            if(i>=strlen(UserName))
            {
                MutualFriends[0]='\0';
                strcat(RetArr,"@");
                strcat(RetArr,itoa(CurrentVertex->UserId,temp,10));
                strcat(RetArr,":");
                strcat(RetArr,CurrentVertex->data.Name);
                strcat(RetArr,":");
                if(IsFriend(UserID,CurrentVertex->UserId))
                {
                    printf("%d",CurrentVertex->UserId);
                    strcat(RetArr,"1");
                }
                else
                {
                    printf("%d",CurrentVertex->UserId);
                    strcat(RetArr,"0");
                }
                strcat(RetArr,"\n");
                PrintMutualFriends(UserID,CurrentVertex->UserId,MutualFriends);
                strcat(RetArr,MutualFriends);
            }
        }
        CurrentVertex=CurrentVertex->next_vertex;
    }
    printf("\n\n\n%s\n",RetArr);
    if(strlen(RetArr)==0)
    {
        strcat(RetArr,"NO USER FOUND");
    }
    return ((env)->NewStringUTF(RetArr));
}


/*char* C_LoadProfile(int UserID)
{
    char RetArr[ARRAY_SIZE],temp[5];
    RetArr[0]='\0';
    vertex* User=NULL;
    char gender[2];
    User=SearchUser(HashFunction(UserID));
    if(User!=NULL)
    {
        gender[0]=User->data.Gender;
        gender[1]='\0';
        strcat(RetArr,itoa(User->data.Age,temp,10));
        strcat(RetArr,":");
        strcat(RetArr,User->data.City);
        strcat(RetArr,":");
        strcat(RetArr,User->data.College);
        strcat(RetArr,":");
        strcat(RetArr,gender);
    }
    printf("\n\nProfile is : %s",RetArr);
    return (RetArr);
}*/

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_LoadProfile
  (JNIEnv *env, jclass, jint ToUserID, jint FromUserID)
{
    char RetArr[ARRAY_SIZE],temp[5];
    RetArr[0]='\0';
    vertex* User=NULL;
    char gender[2];
//    printf("USERID::%d\n",UserID);
    printf("touserid%d\nfromuserid%d\n",ToUserID,FromUserID);
    User=SearchUser(ToUserID);
    int pending=-1;
    if(User!=NULL)
    {
        if(FromUserID!=-1)
        {
            pending=IsPendingRequestBetweenUsers(FromUserID,ToUserID);
        }
        gender[0]=User->data.Gender;
        gender[1]='\0';
        strcat(RetArr,itoa(User->data.Age,temp,10));
        strcat(RetArr,":");
        temp[0]='\0';
        strcat(RetArr,User->data.City);
        strcat(RetArr,":");
        strcat(RetArr,User->data.College);
        strcat(RetArr,":");
        strcat(RetArr,gender);
        strcat(RetArr,":");
        strcat(RetArr,User->PendingFriendsAndStatus->Status);
        if(pending!=-1)
        {
                if(pending==1)
                {
                    strcat(RetArr,":");
                    strcat(RetArr,"2");
                }
                else if(IsPendingRequestBetweenUsers(ToUserID,FromUserID)==1)
                {
                    strcat(RetArr,":");
                    strcat(RetArr,"3");
                }
                else if(IsFriend(FromUserID,ToUserID)==1)
                {
                    strcat(RetArr,":");
                    strcat(RetArr,"1");
                }
                else
                {
                    strcat(RetArr,":");
                    strcat(RetArr,"0");
                }
        }
        else
        {
        	strcat(RetArr,":-1");
        }
    }
		strcat(RetArr,":");
		strcat(RetArr,User->ImagePath);
    printf("\n\nProfile is : %s",RetArr);
    return (env)->NewStringUTF(RetArr);
}


int IsFriend(int UserID1,int UserID2)
{
    vertex* User1=NULL;
    User1=SearchUser(UserID1);
    edge* temp=User1->first_edge;
    int retval=0;
    while(temp!=NULL && retval==0)
    {
        if(((temp)->vertex_point)->UserId==UserID2)
        {
            retval=1;
        }
        temp=temp->next_edge;
    }
    return retval;
}

void CopyPendingRequestsAndStatus(int UserID,int PendingRequest[PENDINGREQUESTSIZE],char Status[STATUS_LEN])
{
    vertex* User=NULL;
    User=SearchUser(UserID);
	if(User!=NULL)
	{
		strcat(User->PendingFriendsAndStatus->Status,Status);
		int i=0;
		User->PendingFriendsAndStatus->PendingFriendRequest[i]=PendingRequest[i];
		while(PendingRequest[i]!=-1)
		{
			User->PendingFriendsAndStatus->PendingFriendRequest[i]=PendingRequest[i];
			i++;
		}
		User->PendingFriendsAndStatus->PendingFriendRequest[i]=-1;
	}
    //printf("\n%s",User->data.Name);
    //printf("\t%s",User->PendingFriendsAndStatus->Status);
    //printf("\t%d",User->PendingFriendsAndStatus->PendingFriendRequest[0]);
}

void ReadPendingRequestAndStatus()
{
    FILE * fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/RequestsAndStatus.db","r");
	int i=0,vertex=0,linked_vertex=0;
    char tmp_vertex[NO_OF_DIGITS_USER_ID];
    tmp_vertex[0]='\0';
    char c,Status[STATUS_LEN],userId[NO_OF_DIGITS_USER_ID];
    userId[0]='\0';
    Status[0]='\0';
    int PendingRequest[PENDINGREQUESTSIZE];
    PendingRequest[0]=-1;
    if(fp!=NULL)
    {
        c=getc(fp);
        while(c!=EOF)
        {
            i=0;
            while(c!='[')
            {
                tmp_vertex[i]=c;
                i++;
                c=getc(fp);
            }
            tmp_vertex[i]='\0';
            vertex=atoi(tmp_vertex);
            c=getc(fp);
            c=getc(fp);
            int j=0;
            while(c!=']')
            {
                i=0;
                while(c!=']' && c!=',')
                {
                    tmp_vertex[i]=c;
                    i++;
                    c=getc(fp);
                }
                tmp_vertex[i]='\0';
                linked_vertex=atoi(tmp_vertex);
                PendingRequest[j]=linked_vertex;
                j++;
                if(c==',')
                {
                    c=getc(fp);
                }
            }
            c=getc(fp);
            i=0;
            while(c!=']')
            {
                Status[i]=c;
                c=getc(fp);
                i++;
            }
            Status[i]='\0';
            PendingRequest[j]=-1;
            CopyPendingRequestsAndStatus(vertex,PendingRequest,Status);
            c=getc(fp);
            if(c=='\n')
            {
                c=getc(fp);
            }
        }
    }
    if(fp!=NULL)
        {
            fclose(fp);
        }
//    free(Status);
    int j=strlen(Status);
    Status[0]='\0';
}

JNIEXPORT void JNICALL Java_org_SocioNet_ServerRunner_SendFriendRequest
  (JNIEnv *, jclass, jint FromUserID, jint ToUserID)
{
    vertex* ToUser;
    ToUser=NULL;
    ToUser=SearchUser(ToUserID);
    int i=0;
    while(ToUser->PendingFriendsAndStatus->PendingFriendRequest[i]!=-1)
    {
        i++;
    }
    ToUser->PendingFriendsAndStatus->PendingFriendRequest[i]=FromUserID;
    ToUser->PendingFriendsAndStatus->PendingFriendRequest[i+1]=-1;
    for(i=0;(ToUser->PendingFriendsAndStatus)->PendingFriendRequest[i]!=-1;i++)
    {
        printf("%d\t",ToUser->PendingFriendsAndStatus->PendingFriendRequest[i]);
    }
}

JNIEXPORT jint JNICALL Java_org_SocioNet_ServerRunner_AcceptRejectRequest
  (JNIEnv *, jclass, jint UserId1, jint UserId2, jint accept)
{
    vertex * user=NULL;
    int i=0,j=0,found=0;
    user=SearchUser(UserId1);
    jint result=0;
    if(accept==1)
    {
        addEdge(UserId1,UserId2,RELATION_FRIEND);
        result=1;
    }
    if(user !=NULL)
    {
        int tmp=-1;
        for(i=0;found==0 && ((tmp=user->PendingFriendsAndStatus->PendingFriendRequest[i])!=-1);i++)
        {
            if(tmp==UserId2)
            {
                for(j=i+1;user->PendingFriendsAndStatus->PendingFriendRequest[j]!=-1;j++)
                {
                    user->PendingFriendsAndStatus->PendingFriendRequest[j-1]=user->PendingFriendsAndStatus->PendingFriendRequest[j];
                }
                user->PendingFriendsAndStatus->PendingFriendRequest[j-1]=-1;
                found=1;
            }
        }
    }
    return result;
}

JNIEXPORT void JNICALL Java_org_SocioNet_ServerRunner_SavePendingStatusesToFile
  (JNIEnv *, jclass)
{
    FILE * fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/RequestsAndStatus.db","w");
    vertex *tmp_vertex=NULL;

    tmp_vertex=Graph->vertex_start;
    int i=0;
    if(fp!=NULL && tmp_vertex!=NULL)
    {
        while(tmp_vertex!=NULL)
        {
            fprintf(fp,"%d[[",tmp_vertex->UserId);
            for(i=0;tmp_vertex->PendingFriendsAndStatus->PendingFriendRequest[i]!=-1;i++)
            {
                fprintf(fp,"%d",tmp_vertex->PendingFriendsAndStatus->PendingFriendRequest[i]);
                if(tmp_vertex->PendingFriendsAndStatus->PendingFriendRequest[i+1]!=-1)
                {
                    fprintf(fp,"%c",',');
                }
            }
            fprintf(fp,"%c",']');
            fprintf(fp,"%s",tmp_vertex->PendingFriendsAndStatus->Status);
            fprintf(fp,"%c",']');
            tmp_vertex=tmp_vertex->next_vertex;
            fprintf(fp,"%c",'\n');
        }
        fclose(fp);
    }
}

int IsPendingRequestBetweenUsers(int FromUserID,int ToUserID)
{
    vertex* ToUser=NULL;
    ToUser=SearchUser(ToUserID);
    int found=0,i=0;
    while(found==0 && ToUser->PendingFriendsAndStatus->PendingFriendRequest[i]!=-1)
    {
        if(ToUser->PendingFriendsAndStatus->PendingFriendRequest[i]==FromUserID)
        {
            found=1;
        }
        i++;
    }
    if(found==1)
    {
        return 1;
    }
    return 0;
}


JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_UpdateCollegeDetails
  (JNIEnv * env, jclass, jint UserID, jstring jcollege)
{

    jboolean boolean;
    const char* college;
    college=(env)->GetStringUTFChars(jcollege,&boolean);
    vertex* User=NULL;
    User=SearchUser(UserID);
    if(User!=NULL)
    {
        User->data.College[0]='\0';
        strcpy(User->data.College,college);
    }
    return (env)->NewStringUTF(college);
}

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_UpdateStatus
  (JNIEnv * env, jclass, jint UserID, jstring jstatus)
{

    jboolean boolean;
    const char* status;
    status=(env)->GetStringUTFChars(jstatus,&boolean);
    vertex* User=NULL;
    User=SearchUser(UserID);
	User->statusTimestamp=time(0);
    if(User->PendingFriendsAndStatus!=NULL)
    {
		User->PendingFriendsAndStatus->Status[0]='\0';
        strcpy(User->PendingFriendsAndStatus->Status,status);
    }
	else
	{
		PendingFriendsStatus* obj = (PendingFriendsStatus *)malloc(sizeof(PendingFriendsStatus));
		strcpy(obj->Status,status);
		User->PendingFriendsAndStatus = obj;
	}
    return (env)->NewStringUTF(status);
}


JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_UpdatePassword
  (JNIEnv * env, jclass, jint UserID, jstring jpassword)
{
    jboolean boolean;
    const char* password;
    password=(env)->GetStringUTFChars(jpassword,&boolean);
    vertex* User=NULL;
    User=SearchUser(UserID);
    if(User!=NULL)
    {
        User->data.Password[0]='\0';
        strcpy(User->data.Password,password);
    }
    return (env)->NewStringUTF(password);
}

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_UpdateCityDetails
  (JNIEnv * env, jclass, jint UserID, jstring jcity)
{
    jboolean boolean;
    const char* city;
    city=(env)->GetStringUTFChars(jcity,&boolean);
	vertex* User=NULL;
    User=SearchUser(UserID);
    if(User!=NULL)
    {
		printf("in city\n");
        User->data.City[0]='\0';
        strcpy(User->data.City,city);
		printf("\n\t%s\n",city);
    }
    return (env)->NewStringUTF(city);
}

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_SendNotification
  (JNIEnv * env, jclass, jint UserID)
{
    vertex* User=NULL;
    vertex* temp_vertex=NULL;
    User=SearchUser(UserID);
    char RetArr[ARRAY_SIZE],temp[5];
    //RetArr[0]='\n';
	RetArr[0]='\0';
    int i=0;
    while(User->PendingFriendsAndStatus->PendingFriendRequest[i]!=-1)
    {
		if(i==0)
		{
			RetArr[0]='\0';
		}
        strcat(RetArr,"FRIENDREQUEST");
        strcat(RetArr,":");
        temp_vertex=(SearchUser(User->PendingFriendsAndStatus->PendingFriendRequest[i]));
        strcat(RetArr,temp_vertex->data.Name);
        strcat(RetArr,":");
        strcat(RetArr,itoa(User->PendingFriendsAndStatus->PendingFriendRequest[i],temp,10));
		strcat(RetArr,": ");
        strcat(RetArr,"\n");
        i++;
    }
    return (env)->NewStringUTF(RetArr);
}

void readMessagesFromFile()
      {
         FILE * fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/Messages.db","r");
         int i=0,vertexno=0,fromuser=0;
         char c,message[MESSAGESIZE],UserId[NO_OF_DIGITS_USER_ID],temp[6];
         UserId[0]='\0';
         message[0]='\0';
         Message* lptr=NULL;
         Message* MessageNode=NULL;
         if(fp!=NULL)
               {
                   c=getc(fp);
                   while(c!=EOF)
                   {
                                    i=0;
                                    while(c!='[')
                                     {
                                          UserId[i]=c;
                                          i++;
                                          c=getc(fp);
                                     }
                                    UserId[i]='\0';
                                    vertexno=atoi(UserId);
                                    c=getc(fp);
                                    i=0;

                                    while(c!=']')
                                    {
                                                i=0;
                                                while(c!=':')
                                                {
                                                      UserId[i]=c;
                                                      i++;
                                                      c=getc(fp);
                                                }
                                                UserId[i]='\0';
                                                fromuser=atoi(UserId);
                                                c=getc(fp);
                                                i=0;

                                                while(c!=',' && c!=']')
                                                {
                                                      message[i]=c;
                                                      i++;
                                                      c=getc(fp);
                                                }
                                                message[i]='\0';
                                                MessageNode=(Message*)malloc(sizeof(Message));
                                                if(MessageNode!=NULL)
                                                      {
                                                            MessageNode->FromUserID=fromuser;
                                                            MessageNode->IsRead=false;
                                                            MessageNode->NextMessage=NULL;
                                                            strcpy(MessageNode->MessageBody,message);
                                                            lptr=AddMessageToList(lptr,MessageNode);
                                                      }
                                                if(c==',')
                                                      {
														c=getc(fp);
                                                      }
                                    }
                                    AddMessageToUser(vertexno,lptr);
                                    c=getc(fp);
                                    if(c=='\n')
                                    {
                                          c=getc(fp);
                                          lptr=NULL;
                                          MessageNode=NULL;
                                          vertexno=0;
                                    }
                   }
               }
      }

Message* AddMessageToList(Message* lptr,Message* MessageNode)
{
      Message* temp=NULL;
      temp=lptr;
	  int max=-1;
      if(lptr!=NULL)
      {
				   while(temp->NextMessage!=NULL && temp->IsRead==false)
					{
						temp=temp->NextMessage;
						if(max<temp->messageID)
						{
							max=temp->messageID;
						}
					}
					if(temp->NextMessage==NULL)
					{
						temp->NextMessage=MessageNode;
						if(temp->messageID!=-1)
						{
							MessageNode->messageID=temp->messageID + 1;
						}
						else
						{
							MessageNode->messageID=max+1;
						}
					}
					else if(temp->IsRead==true)
					{
						temp->FromUserID=MessageNode->FromUserID;
						temp->IsRead=false;
						strcpy(temp->MessageBody,MessageNode->MessageBody);
					}
         }
         else
         {
					lptr=MessageNode;
					MessageNode->messageID=1;
					printf("In else%s\n",MessageNode->MessageBody);
         }
         return lptr;
}

void markMessageRead(int UserID,int MessageID)
{
	vertex* User=NULL;
	User=SearchUser(UserID);
	Message* temp=NULL;
	if(User!=NULL)
	{
		temp=User->UserMessages;
		if(temp!=NULL)
		{
			while(temp->messageID!=MessageID && temp!=NULL)
			{
				temp=temp->NextMessage;
			}
			if(temp!=NULL)
			{
				temp->IsRead=true;
				temp->messageID=-1;
			}
		}
	}
}
void AddMessageToUser(int vertexno,Message* lptr)
{
           vertex* User=NULL;
           User=SearchUser((vertexno));
           if(User!=NULL)
                 {
                      User->UserMessages=lptr;
                 }
}

JNIEXPORT void JNICALL Java_org_SocioNet_ServerRunner_WriteMessagesToFile
  (JNIEnv *, jclass)
{
        vertex* user=NULL;
	  user=Graph->vertex_start;
	  FILE* fp;
	  fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/Messages.db","w");
	  while(user!=NULL && fp!=NULL)
	  {
	      fprintf(fp,"%d[",user->UserId);
		Message* temp=user->UserMessages;
		while(temp!=NULL)
		{
		     fprintf(fp,"%d:%s",temp->FromUserID,temp->MessageBody);
		     if(temp->NextMessage!=NULL)
		     {
		         fprintf(fp,",");
		     }
		     temp=temp->NextMessage;
		}
		fprintf(fp,"]\n");
		user=user->next_vertex;
	  }
	  fclose(fp);
}

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_SendMessageToUser
  (JNIEnv * env, jclass, jint FromUserID, jint ToUserID, jstring jMessageBody)
{
	jboolean boolean;
    const char* MessageBody;
    MessageBody=(env)->GetStringUTFChars(jMessageBody,&boolean);
    vertex* user=NULL;
    user=SearchUser(ToUserID);
    Message* temp=NULL;
    temp=user->UserMessages;
    int MessageCount=0;char retArr[20];
	retArr[0]='\0';
    Message* NewMessage=(Message*)malloc(sizeof(Message));
	  if(NewMessage!=NULL)
	  {
		NewMessage->FromUserID=FromUserID;
		NewMessage->IsRead=false;
		NewMessage->NextMessage=NULL;
		NewMessage->MessageBody[0]='\0';
		NewMessage->messageID=-1;
		strcpy(NewMessage->MessageBody,MessageBody);
		//temp->NextMessage=NewMessage;
		user->UserMessages=AddMessageToList(user->UserMessages,NewMessage);
		strcpy(retArr,"MESSAGESENT");
	  }
	  return (env)->NewStringUTF(retArr);
}

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_GetAllMessages
  (JNIEnv * env, jclass, jint UserID)
{
   vertex* user=NULL;
   user=SearchUser(UserID);
   int i=0;
   char RetArr[BIGARRAYSIZE],tmp[10];
   RetArr[0]='\n';
   RetArr[1]='\0';
   Message* temp=NULL;
   if(user!=NULL)
   {
       temp=user->UserMessages;
       while(temp!=NULL)
	   {
		 if(i==0)
		 {
			RetArr[0]='\0';
		 }
		 strcat(RetArr,"MESSAGES");
		 strcat(RetArr,":");
	     strcat(RetArr,SearchUser(temp->FromUserID)->data.Name);
	     strcat(RetArr,":");
	     strcat(RetArr,itoa(temp->messageID,tmp,10));
		 strcat(RetArr,":");
		 strcat(RetArr,temp->MessageBody);
	     if(temp->NextMessage!=NULL)
	     {
	        strcat(RetArr,"\n");
	     }
	     temp=temp->NextMessage;
		 i++;
	   }
	   printf("\n\nmessage:-%s\n\n",RetArr);
   }
   return (env)->NewStringUTF(RetArr);
}





JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_breadthFirst
  (JNIEnv * env, jclass, jint UserId, jstring username)
{
	jboolean boolean;
    const char* test;
    test=(env)->GetStringUTFChars(username,&boolean);
	printf("\n\n\t%s\n\n",test);
	Queue *q;
	char retcityAlpha[1000]="";
    int *visited=(int *)malloc((sizeof(int))*Graph->no_vertices);
    int a,count=0;
	int constraint;
	constraint=giveSearchConstraint();
	q=(Queue *)malloc(sizeof(Queue));
    Initialize_Queue(q);
    edge *e;
    vertex *w,*v,*temp;
	status_code SC=SUCCESS;
	v=SearchUser(UserId);
	if(v==NULL)
	{
		retcityAlpha[0]='\0';
		return (env)->NewStringUTF(retcityAlpha);;
	}
	if(v->first_edge==NULL)
	{
		UserId=getUserId(v);
		v=SearchUser(UserId);
		if(v==NULL)
		{
			retcityAlpha[0]='\0';
			return (env)->NewStringUTF(retcityAlpha);;
		}
	}
	temp=v;
    w=v;
    while(v!=NULL)
    {
        visited[v->UserId]=0;
        v=v->next_vertex;
		count ++;
    }
	count=0;
	v=temp;
    Visit(v);
    visited[v->UserId]=1;
    e=v->first_edge;
    while(e!=NULL && count < constraint)
    {
		if(strstr(e->vertex_point->data.Name,test)!=NULL && visited[e->vertex_point->UserId]!=1)
        {
			addToResult(retcityAlpha,UserId,e->vertex_point);
        }
        if(count < constraint)
        {
            SC=Insert_Queue(q,e->vertex_point->UserId);
            visited[e->vertex_point->UserId]=1;
            e=e->next_edge;
			count ++;
        }
    }
    SC=SUCCESS;
    while(!isEmptyQueue(q) && count < constraint/*SC==SUCCESS*/)
    {
        SC=Delete_Queue(q,&a);
        w=SearchUser(a);
        e=w->first_edge;
        while(e!=NULL)
        {
            if(strstr(e->vertex_point->data.Name,test)!=NULL && visited[e->vertex_point->UserId]!=1)
            {
				addToResult(retcityAlpha,UserId,e->vertex_point);
            }
            SC=Insert_Queue(q,e->vertex_point->UserId);
            visited[e->vertex_point->UserId]=1;
			count ++;
            e=e->next_edge;
        }
    }
	return (env)->NewStringUTF(retcityAlpha);
}

void addToResult(char retArr[1000],int UserId,vertex *v)//-----------------------CHANGED
{
	char temp[10];
	int isFrnd,num;
	isFrnd=IsFriend(UserId,v->UserId);
	num=countMutualFriend(UserId,v->UserId);
	//strcat(retArr,"@");
	strcat(retArr,itoa(v->UserId,temp,10));
	strcat(retArr,":");
	strcat(retArr,v->data.Name);
	strcat(retArr,":");
	strcat(retArr,itoa(isFrnd,temp,10));
	strcat(retArr,":");
	strcat(retArr,itoa(num,temp,10));
	strcat(retArr,":");
	strcat(retArr,v->ImagePath);
	strcat(retArr,"\n");
}

int giveSearchConstraint()
{
	int ret_val=0;
	int num=Graph->no_vertices;
	if(num < 150)
		ret_val=60;
	else if(num >= 150 && num < 300)
		ret_val=90;
	else if(num >= 300 && num < 500)
		ret_val=130;
	else if(num >= 500 && num < 1000)
		ret_val=225;
	else if(num >= 1000 && num < 3000)
		ret_val=400;
	else if(num >= 3000 && num < 8000)
		ret_val=700;
	else if(num >=8000)
		ret_val=1500;
	return ret_val;
}

void Initialize_Queue(Queue *qptr)
{
    qptr->front=NULL;
    qptr->rear=NULL;
}

int isEmptyQueue(Queue *qptr)
{
    return(qptr->front==NULL);
}
status_code Insert_Queue(Queue *qptr,int d)
{
    status_code S=SUCCESS;
    Node *nptr;
    nptr=(Node *)malloc(sizeof(Node));
    if(nptr==NULL)
    {
        S=FAILURE;
    }
    else
    {
        nptr->data=d;
        nptr->next=NULL;
        if(isEmptyQueue(qptr))
        {
            qptr->front=qptr->rear=nptr;
        }
        else
        {
            (qptr->rear)->next=nptr;
            qptr->rear=nptr;
        }

    }
    return(S);
}

status_code Delete_Queue(Queue *qptr,int *dptr)
{
    status_code S=SUCCESS;
    Node *nptr;
    if(isEmptyQueue(qptr))
    {
        S=FAILURE;
    }
    else
    {
        nptr=qptr->front;
        qptr->front=nptr->next;
        *dptr=nptr->data;
        free(nptr);
        if(qptr->front==NULL)
        {
            qptr->rear=NULL;
        }
    }
    return(S);
}


void Visit(vertex *v)
{
    printf("%d\n",v->UserId);
}


int countMutualFriend(int UserID1,int UserID2)
{
	int ret_val = 0;
	vertex* User1=NULL;
    vertex* User2=NULL;
    User1=SearchUser(UserID1);
    User2=SearchUser(UserID2);
	edge* EdgeIterator1=User1->first_edge;
    edge* EdgeIterator2=User2->first_edge;
    char temp[NO_OF_DIGITS_USER_ID];
    while(EdgeIterator1!=NULL)
    {
        EdgeIterator2=User2->first_edge;
        while(EdgeIterator2!=NULL)
        {
            if(EdgeIterator2->vertex_point==EdgeIterator1->vertex_point)
            {
                ret_val ++;
            }
            EdgeIterator2=EdgeIterator2->next_edge;
        }
        EdgeIterator1=EdgeIterator1->next_edge;
    }
	return ret_val;
}

JNIEXPORT void JNICALL Java_org_SocioNet_ServerRunner_updateProfilePic
  (JNIEnv * env, jclass, jint UserID, jstring jPath)
{
	jboolean boolean;
    const char* Path;
    Path=(env)->GetStringUTFChars(jPath,&boolean);
	vertex* User=NULL;
	User=SearchUser(UserID);
	if(User!=NULL)
	{
		strcpy(User->ImagePath,Path);
	}
}

JNIEXPORT jint JNICALL Java_org_SocioNet_ServerRunner_getNoOfVertices
  (JNIEnv *, jclass)
{
	printf("\n\n%d\n\n",Graph->no_vertices);
	return (Graph->no_vertices);
}
JNIEXPORT jintArray JNICALL Java_org_SocioNet_ServerRunner_getEdges
  (JNIEnv * env, jclass, jint UserID)
{
	vertex* User=NULL;
	edge* edgeIterator=NULL;
	jintArray result;
	jint retArr[40];
	int i=0;
	for(i=0;i<40;i++)
	{
		retArr[i]=-1;
	}
	i=0;
	result = (env)->NewIntArray(40);
	User=SearchUser(UserID);
	edgeIterator=User->first_edge;
	while(edgeIterator!=NULL)
	{
		retArr[i]=edgeIterator->vertex_point->UserId;
		i++;
		edgeIterator=edgeIterator->next_edge;
	}
	(env)->SetIntArrayRegion(result, 0, 40, retArr);
	return result;
}

int getUserId(vertex *v)
{
	int posInAlphaArray=0,retUserId=0,found=0,listIndex=0;
	char city[CITY_LEN],firstChar,chkCity[CITY_LEN];
	city_list *cityListForAlpha;
	vertex *firstInCity=NULL;
	strcpy(city,v->data.City);
	firstChar=city[0];
	posInAlphaArray=convertAlphaToInt(firstChar);
	cityListForAlpha=cityAlpha[posInAlphaArray].list;
	strcpy(chkCity,cityListForAlpha->city_name);
	while(strcmp(city,chkCity)!=0 && cityListForAlpha!=NULL)
	{
		cityListForAlpha=cityListForAlpha->next;
		strcpy(chkCity,cityListForAlpha->city_name);
	}
	if(cityListForAlpha->vptr!=NULL)
	{
		retUserId=cityListForAlpha->vptr->UserId;
		if(retUserId==v->UserId)
		{
			if(cityListForAlpha->vptr->next_vertex!=NULL)
			{
				retUserId=cityListForAlpha->vptr->next_vertex->UserId;
			}
			else
			{
				retUserId=1;
			}
		}
	}
	if(retUserId==v->UserId)
	{
		if(cityAlpha[posInAlphaArray].list->vptr->next_vertex!=NULL)
			retUserId=cityAlpha[posInAlphaArray].list->vptr->next_vertex->UserId;
		else
			retUserId=1;
	}
	return retUserId;
}

int convertAlphaToInt(char c)
{
	int ret_val=0;
	ret_val=c-'0';
	if(ret_val>=17 && ret_val<=42)
	{
                   ret_val=ret_val-17;
    }
    else if(ret_val>=49 && ret_val<=74)
    {
                   ret_val=ret_val-49;
    }
	return ret_val;
}

city_list* createCity(char city[])
{
	city_list *new_list=NULL;
	new_list=(city_list *)malloc(sizeof (city_list));
	new_list->vptr=NULL;
	new_list->last_vertex=NULL;
	new_list->next=NULL;
	strcpy(new_list->city_name,city);
	return new_list;
}

void createLastLink(vertex *v,char city[],int pos,int deletion)
{
	int flag=0,lastAlpha=0;
	city_list *preprev,*prev,*curr;
	vertex *temp=NULL;
	prev=NULL;
	preprev=NULL;
	curr=cityAlpha[pos].list;
	while(flag==0)
	{
		if(strcmp(curr->city_name,city)==0)
			flag=1;
		else
			prev=curr;
		curr=curr->next;
	}
	if(flag==1)
	{
		if(prev!=NULL && deletion==0)
		{
			prev->last_vertex->next_vertex=v;
		}
		else if(deletion==1 && prev!=NULL)
		{
			prev->last_vertex->next_vertex=v->next_vertex;
		}
	}
	if(prev==NULL)
	{
		lastAlpha=pos-1;
		while(cityAlpha[lastAlpha].list==NULL && lastAlpha>=0)
		{
			lastAlpha--;
		}
		if(lastAlpha>=0)
		{
			prev=NULL;
			curr=cityAlpha[lastAlpha].list;
			while(curr!=NULL)
			{
				prev=curr;
				curr=curr->next;
			}
			if(deletion==0)
			{
				v->next_vertex=prev->last_vertex->next_vertex;
				prev->last_vertex->next_vertex=v;

			}
			else
			{
				prev->last_vertex->next_vertex=v->next_vertex;
			}
		}
		else
		{
			Graph->vertex_start=v;
			if(deletion==1)
			{
				Graph->vertex_start=v->next_vertex;
			}
		}
	}
}

void createNextLink(vertex *v,char city[],int pos)
{
	int flag=0,i=0;
	city_list *curr,*prev;
	curr=cityAlpha[pos].list;
	while(flag==0)
	{
		if(strcmp(curr->city_name,city)==0)
			flag=1;
		else
			curr=curr->next;
	}
	if(curr->next!=NULL)
	{
		curr=curr->next;
		v->next_vertex=curr->vptr;
	}
	else
	{
		i=pos+1;
		while(cityAlpha[i].list==NULL && i<26)
		{
			i++;
		}
		if(i<26)
		{
			curr=cityAlpha[i].list;
			v->next_vertex=curr->vptr;
		}
		else
		{
			v->next_vertex=NULL;
		}
	}
}

void initialise_alphaArray()
{
	int i=0;
	for(i=0;i<26;i++)
	{
		cityAlpha[i].alphabet='\0';
		cityAlpha[i].list=NULL;
	}
}

void deleteVertexStructure(vertex *v)
{
	int pos,flag=0;
	char city[CITY_LEN],c;
	city_list *curr_city,*prev_city;
	vertex *curr_vertex,*prev_vertex;
	curr_vertex=NULL;
	prev_vertex=NULL;
	curr_city=NULL;
	prev_city=NULL;
	strcpy(city,v->data.City);
	c=city[0];
	pos=convertAlphaToInt(c);
	curr_city=cityAlpha[pos].list;
	while(flag==0)
	{
		if(strstr(curr_city->city_name,city)!=NULL)
		{
			flag=1;
		}
		else
		{
			prev_city=curr_city;
			curr_city=curr_city->next;
		}
	}
	if(curr_city->vptr==curr_city->last_vertex)
	{
		createLastLink(v,city,pos,1);
		if(prev_city==NULL)
		{
			cityAlpha[pos].list=curr_city->next;
		}
		else
		{
			prev_city->next=curr_city->next;
		}
		curr_city->vptr=NULL;
		curr_city->last_vertex=NULL;
		free(v);
		free(curr_city);
	}
	else
	{
		if(curr_city->vptr==v)
		{
			createLastLink(v,city,pos,1);
			curr_city->vptr=v->next_vertex;
			free(v);
		}
		else
		{
			curr_vertex=curr_city->vptr;
			while(curr_vertex!=v)
			{
				prev_vertex=curr_vertex;
				curr_vertex=curr_vertex->next_vertex;
			}
			prev_vertex->next_vertex=curr_vertex->next_vertex;
			if(curr_vertex==curr_city->last_vertex)
			{
				curr_city->last_vertex=prev_vertex;
			}
			free(curr_vertex);
		}
	}
}

group* createGroup(char groupName[],int adminID,int groupID)//--------------------------------------------------------------------------------------------------
{
	group*newGroup=NULL;
	newGroup=(group*)malloc(sizeof(group));
	if(newGroup!=NULL)
	{
		newGroup->adminID=adminID;
		if(groupID==-1)
		{
			groupID=Graph->lastAddedGroupID +1;
		}
		newGroup->groupID=groupID;
		strcpy(newGroup->groupName,groupName);
		newGroup->groupMembers=NULL;
		newGroup->groupPosts=NULL;
		newGroup->nextGroup=NULL;
		newGroup->groupPendingRequests=NULL;
	}
	return newGroup;
}
post* createPost(int userID,long int timestamp,char postContent[])//--------------------------------------------------------------------------------------------------
{
	post* newPost=NULL;
	newPost=(post*)malloc(sizeof(post));
	if(newPost!=NULL)
	{
		newPost->postUserID=userID;
		strcpy(newPost->postContent,postContent);
		newPost->timestamp=timestamp;
		newPost->nextPost=NULL;
	}
	return newPost;
}
members* createNewMembersSet(int membersID[],int low,int high)//--------------------------------------------------------------------------------------------------
{
	members* newSet=NULL;
	newSet=(members*)malloc(sizeof(members));
	if(newSet!=NULL)
	{
		int i=low,j=0;
		while(i!=high)
		{
			if(membersID[i]>0)
			{
				newSet->membersID[j]=membersID[i];
				j++;
				i++;
			}
		}
		newSet->count=high;
		newSet->next=NULL;
	}
	return newSet;
}

adminPendingRequests* createPendingRequestsSet(int membersID[],int low,int high,int groupID)//--------------------------------------------------------------------------------------------------
{
	adminPendingRequests* newSet=NULL;
	newSet=(adminPendingRequests*)malloc(sizeof(adminPendingRequests));
	if(newSet!=NULL)
	{
		int i=low,j=0;
		while(i!=high)
		{
			if(membersID[i]>0)
			{
				newSet->membersID[j]=membersID[i];
				j++;
				i++;
			}
		}
		newSet->groupID=groupID;
		newSet->count=high;
		newSet->next=NULL;
	}
	return newSet;
}

members* addToList(members* lptr,members* node)//--------------------------------------------------------------------------------------------------
{
	if(lptr==NULL)
	{
		lptr=node;
	}
	else
	{
		node->next=lptr;
		lptr=node;
	}
	return lptr;
}

post* addPostToList(post* lptr,post* node)//--------------------------------------------------------------------------------------------------
{
	if(lptr==NULL)
	{
		lptr=node;
	}
	else
	{
		node->nextPost=lptr;
		lptr=node;
	}
	return lptr;
}

group* addGroupToList(group* lptr,group* node)//--------------------------------------------------------------------------------------------------
{
	if(lptr==NULL)
	{
		lptr=node;
	}
	else
	{
		node->nextGroup=lptr;
		lptr=node;
	}
	return lptr;
}

void addGroupsToGraph(group* groupList)//--------------------------------------------------------------------------------------------------
{
	if(Graph!=NULL)
	{
		Graph->groups=groupList;
	}
}

adminPendingRequests* addPendingRequestsNodeToList(adminPendingRequests* lptr,adminPendingRequests* node)//--------------------------------------------------------------------------------------------------
{
	if(lptr==NULL)
	{
		lptr=node;
	}
	else
	{
		node->next=lptr;
		lptr=node;
	}
	return lptr;
}

void addPendingRequestsListToGroup(int groupID,adminPendingRequests* pendingRequestList)//--------------------------------------------------------------------------------------------------
{
	group* thisGroup=NULL;
	thisGroup=searchGroup(groupID);
	if(thisGroup!=NULL)
	{
		thisGroup->groupPendingRequests=pendingRequestList;
	}
}

void readGroups()//--------------------------------------------------------------------------------------------------
{
	FILE *fp;
	char c;
	int i=0,groupIDint,adminIDint,temp,j=0,membersID[20],postUserID;
	long int timestampLong=0;
	char groupID[6],adminID[6],groupName[20],timestamp[20],postContent[POSTSIZE];
	adminPendingRequests* newPendingRequestsNode=NULL;
	adminPendingRequests* pendingRequestList=NULL;
	members* membersNode=NULL;
	members* membersList=NULL;
	post* postList=NULL;
	post* newPost=NULL;
	group* groupList=NULL;
	group* newGroup=NULL;
	fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/groups.db","r");
	createGroupHashTable(Hash_TABLE_SIZE);
	if(fp!=NULL)
	{
		c=getc(fp);
		while(c!=EOF)
		{
			while(c!=',')
			{
				groupID[i]=c;
				c=getc(fp);
				i++;
			}
			groupID[i]='\0';
			groupIDint=atoi(groupID);
			i=0;
			c=getc(fp);
			while(c!=',')
			{
				adminID[i]=c;
				c=getc(fp);
				i++;
			}
			adminID[i]='\0';
			adminIDint=atoi(adminID);
			i=0;
			c=getc(fp);
			while(c!='[')
			{
				groupName[i]=c;
				c=getc(fp);
				i++;
			}
			groupName[i]='\0';
			i=0;
			c=getc(fp);
			while(c!=']')
			{
				while(c!=',' && c!=']')
				{
					groupID[i]=c;
					c=getc(fp);
					i++;
				}
				groupID[i]='\0';
				membersID[j]=atoi(groupID);
				int userID=membersID[j];
				vertex* user=SearchUser(userID);
				int p=0;
				if(user!=NULL)
				{
					while(user->memberOfGroups[p]!=-1)
					{
						p++;
					}
					if(p<MAXNUMBEROFGROUPS-1)
					{
						user->memberOfGroups[p]=groupIDint;
						user->memberOfGroups[p+1]=-1;
					}
				}
				j++;
				if(j==MEMBERSCOUNTSIZE)
				{
					membersNode=createNewMembersSet(membersID,0,j);
					membersList=addToList(membersList,membersNode);
					j=0;
				}
				i=0;
				if(c!=']')
				{
					c=getc(fp);
				}
			}
			if(j!=0)
			{
				membersNode=createNewMembersSet(membersID,0,j);
				membersList=addToList(membersList,membersNode);
				j=0;
			}
			c=getc(fp);
			i=0;
			while(c!=']')
			{
				c=getc(fp);
				if(c=='{')
				{
					c=getc(fp);
				}
				while(c!='}' && c!=']')
				{
					while(c!=',')
					{
						groupID[i]=c;
						c=getc(fp);
						i++;
					}
					groupID[i]='\0';
					i=0;
					c=getc(fp);
					postUserID=atoi(groupID);
					while(c!=',')
					{
						postContent[i]=c;
						c=getc(fp);
						i++;
					}
					postContent[i]='\0';
					i=0;
					c=getc(fp);
					while(c!='}')
					{
						timestamp[i]=c;
						c=getc(fp);
						i++;
					}
					timestamp[i]='\0';
					i=0;
					timestampLong=atol(timestamp);
					newPost=createPost(postUserID,timestampLong,postContent);
					postList=addPostToList(postList,newPost);
				}
				if(c!=']')
				{
					c=getc(fp);
				}
			}
			newGroup=createGroup(groupName,adminIDint,groupIDint);
			newGroup->groupMembers=membersList;
			newGroup->groupPosts=postList;
			groupList=addGroupToList(groupList,newGroup);
			groupTable* newPair=createNewGroupPair(groupIDint,newGroup);
			addNewGroupPair(newPair);
			c=getc(fp);
			if(c=='[')
			{
				c=getc(fp);
			}
			j=0;
			while(c!=']')
			{
				i=0;
				while(c!=',' && c!=']')
				{
					groupID[i]=c;
					c=getc(fp);
					i++;
				}
				groupID[i]='\0';
				membersID[j]=atoi(groupID);
				j++;
				if(j==MEMBERSCOUNTSIZE)
				{
					newPendingRequestsNode=createPendingRequestsSet(membersID,0,j,groupIDint);
					pendingRequestList=addPendingRequestsNodeToList(pendingRequestList,newPendingRequestsNode);
					j=0;
				}
				if(c!=']')
				{
					c=getc(fp);
				}
			}
			if(j!=0)
			{
				newPendingRequestsNode=createPendingRequestsSet(membersID,0,j,groupIDint);
				pendingRequestList=addPendingRequestsNodeToList(pendingRequestList,newPendingRequestsNode);
				j=0;
			}
			addPendingRequestsListToGroup(groupIDint,pendingRequestList);
			Graph->lastAddedGroupID=groupIDint;
			c=getc(fp);
			if(c=='\n')
			{
				c=getc(fp);
			}
			membersList=NULL;
			postList=NULL;
			pendingRequestList=NULL;
			groupID[0]='\0';
			adminID[0]='\0';
			groupName[0]='\0';
			timestamp[0]='\0';
			postContent[0]='\0';
			i=0;
			j=0;
		}
	}
	//------------------------------------------------------ADD TO GRAPH
	addGroupsToGraph(groupList);
}

void printGroups()//--------------------------------------------------------------------------------------------------
{
	group* tempGroup;
	members* tempMembers;
	post* tempPosts;
	if(Graph!=NULL)
	{
		tempGroup=Graph->groups;
		if(tempGroup!=NULL)
		{
			while(tempGroup!=NULL)
			{
				printf("\t%d\t%s\t%d\n",tempGroup->groupID,tempGroup->groupName,tempGroup->adminID);
				tempMembers=tempGroup->groupMembers;
				tempPosts=tempGroup->groupPosts;
				tempGroup=tempGroup->nextGroup;
			}
		}
	}
}

void writeGroupsToFile()//--------------------------------------------------------------------------------------------------
{
	FILE* fp=NULL;
	fp=fopen("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/DB/out.db","w");
	if(fp!=NULL)
	{
		group* tempGroup;
		members* tempMembers;
		post* tempPosts;
		adminPendingRequests* tempRequests;
		if(Graph!=NULL)
		{
			tempGroup=Graph->groups;
			if(tempGroup!=NULL)
			{
				while(tempGroup!=NULL)
				{
					fprintf(fp,"%d,%d,%s[",tempGroup->groupID,tempGroup->adminID,tempGroup->groupName);
					tempMembers=tempGroup->groupMembers;
						while(tempMembers!=NULL)
						{
							int i=0;
							for(i=0;i<tempMembers->count;i++)
							{
								fprintf(fp,"%d",tempMembers->membersID[i]);
								if(tempMembers->next!=NULL || i!=tempMembers->count-1)
								{
									fprintf(fp,",");
								}
							}
							tempMembers=tempMembers->next;
						}
						fprintf(fp,"][");
					tempPosts=tempGroup->groupPosts;
						while(tempPosts!=NULL)
						{
							fprintf(fp,"{%d,%s,%ld}",tempPosts->postUserID,tempPosts->postContent,tempPosts->timestamp);
							if(tempPosts->nextPost!=NULL)
							{
								fprintf(fp,",");
							}
							tempPosts=tempPosts->nextPost;
						}
						fprintf(fp,"][");
						tempRequests=tempGroup->groupPendingRequests;
						while(tempRequests!=NULL)
						{
							int i=0;
							for(i=0;i<tempRequests->count;i++)
							{
								fprintf(fp,"%d",tempRequests->membersID[i]);
								if(tempRequests->next!=NULL || i!=tempRequests->count-1)
								{
									fprintf(fp,",");
								}
							}
							tempRequests=tempRequests->next;
						}
						fprintf(fp,"]\n");
					tempGroup=tempGroup->nextGroup;
				}
			}
		}
	}
	fclose(fp);
}

void createGroupHashTable(int size)//--------------------------------------------------------------------------------------------------
{
    if(size>=1)
    {
        ght=(groupHashTable*)malloc(sizeof(groupHashTable));
        ght->size=size;
        int i=0;
        for(i=0;i<ht->size;i++)
        {
            ght->table[i]=NULL;
        }
    }
}


void viewGroupHashTable()//--------------------------------------------------------------------------------------------------
{
    int i=0;
    while(i<ght->size)
    {
        groupTable* TableIterator=NULL;
        TableIterator=ght->table[i];
        while(TableIterator!=NULL)
        {
            printf("%s\t",(TableIterator->groupPointer)->groupName);
            TableIterator=TableIterator->next;
        }
        i++;
        printf("\n");
    }
}

groupTable* createNewGroupPair(int key,group* groupPointer)//--------------------------------------------------------------------------------------------------
{
    groupTable* newPair=NULL;
    newPair=(groupTable*)malloc(sizeof(groupTable));
	if(newPair!=NULL)
	{
		newPair->key=key;
		newPair->groupPointer=groupPointer;
		newPair->next=NULL;
	}
    return newPair;
}

void addNewGroupPair(groupTable* NewPair)//--------------------------------------------------------------------------------------------------
{
    if(ght!=NULL)
    {
        groupTable* AddingTable=NULL;
        int k=HashFunction(NewPair->key);
        AddingTable=(ght->table[k]);
        if(AddingTable==NULL)
        {
            ght->table[k]=NewPair;
        }
        else
        {
            while(AddingTable->next!=NULL)
            {
                AddingTable=AddingTable->next;
            }
            AddingTable->next=NewPair;
        }
    }
    else
    {
        printf("problem!!!!");
    }
}

group* searchGroup(int groupID)//--------------------------------------------------------------------------------------------------
{
    groupTable* SearchingTable=ght->table[HashFunction(groupID)];
    group* retval=NULL;
    if(SearchingTable!=NULL)
    {
        while(retval==0 && SearchingTable!=NULL)
        {
            if(SearchingTable->key==groupID)
            {
                retval=SearchingTable->groupPointer;
            }
            else
            {
                SearchingTable=SearchingTable->next;
            }
        }
    }
    return retval;
}

int groupExists(char groupName[])//--------------------------------------------------------------------------------------------------
{
	int retval=0;
	group* temp;
	if(Graph!=NULL)
	{
		if(Graph->groups!=NULL)
		{
			temp=Graph->groups;
			while(temp!=NULL && retval==0)
			{
				if(strcmp(temp->groupName,groupName)==0)
				{
					retval=1;
				}
				else
				{
					temp=temp->nextGroup;
				}
			}
		}
	}
	return retval;
}
//char* createNewGroupJNI(int adminID,char groupName[])//--------------------------------------------------------------------------------------------------
JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_createNewGroup
  (JNIEnv * env, jclass, jint adminID, jstring jgroupName)
{
	jboolean boolean;
	const char* grpName=(env)->GetStringUTFChars(jgroupName,&boolean);
	char groupName[50];
	strcpy(groupName,grpName);
	char retArr[100];
	char temp[5];
	retArr[0]='\0';
	if(!groupExists(groupName))
	{
		int groupID=Graph->lastAddedGroupID+1;
		group* newGroup=createGroup(groupName,adminID,groupID);
		if(newGroup!=NULL)
		{
			groupTable* newPair=createNewGroupPair(groupID,newGroup);
			addNewGroupPair(newPair);
			if(Graph!=NULL)
			{
				group* lptr=Graph->groups;
				if(lptr==NULL)
				{
					lptr=newGroup;
				}
				else
				{
					newGroup->nextGroup=lptr;
					lptr=newGroup;
				}
				Graph->groups=lptr;
				strcat(retArr,"GROUPCREATED");
				strcat(retArr,":");
				strcat(retArr,groupName);
				strcat(retArr,":");
				strcat(retArr,itoa(adminID,temp,10));
				strcat(retArr,":");
				strcat(retArr,itoa(groupID,temp,10));
			}
		}
	}
	else
	{
		strcpy(retArr,"GROUPEXISTS:");
	}
	return (env)->NewStringUTF(retArr);
}


JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_acceptOrRejectGroupRequest
  (JNIEnv * env, jclass, jint groupID, jint fromUserID, jint ofUserID, jint accept)
{
	group* tempGroup;
	tempGroup=searchGroup(groupID);
	char retArr[100];
	retArr[0]='\0';
	if(tempGroup!=NULL)
	{
		if(tempGroup->adminID==fromUserID)
		{
			int k;
			int searchID=-1,done=0;
			adminPendingRequests* tempRequests=tempGroup->groupPendingRequests;
			while(tempRequests!=NULL && searchID==-1 && done==0)
			{
				k=0;
				while(tempRequests!=NULL && k<tempRequests->count && done==0)
				{
					if(tempRequests->membersID[k]==ofUserID)
					{
						done=1;
					}
					else
					{
						k++;
					}
				}
				if(done==0)
				{
					tempRequests=tempRequests->next;
				}
			}
			if(done==1)
			{
				int i=k;
				for(i=k;i<tempRequests->count-1;i++)
				{
					tempRequests->membersID[i]=tempRequests->membersID[i+1];
				}
				tempRequests->count--;
			}
			if(accept==1)
			{
				vertex* user=SearchUser(ofUserID);
				if(user!=NULL)
				{
					k=0;
					while(user->memberOfGroups[k]!=-1 && k<MAXNUMBEROFGROUPS)
					{
						k++;
					}
					user->memberOfGroups[k]=groupID;
					user->memberOfGroups[k+1]=-1;
					strcat(retArr,"ACCEPTED:");
				}
			}
		}
		else
		{
			strcat(retArr,"YOUARENOTADMIN:");
		}
	}
	else
	{
		strcat(retArr,"GROUPDOESNOTEXIST:");
	}
	return (env)->NewStringUTF(retArr);
}

//acceptOrRejectGroupRequest(int groupID,int fromUserID,int ofUserID,int accept)
//createNewGroupJNI(int adminID,char groupName[])
//groupExists(char groupName[])
//searchGroup(int groupID)

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_addMemberToGroup
  (JNIEnv * env, jclass, jint groupID, jint userID)
{
	group* tempGroup;
	tempGroup=searchGroup(groupID);
	char retArr[100];
	retArr[0]='\0';
	int done=0;
	if(tempGroup!=NULL)
	{
		members* tempMembers=tempGroup->groupMembers;
		if(tempMembers!=NULL)
		{
			int i;
			while(tempMembers!=NULL && done==0)
			{
				i=0;
				for(i=0;i<tempMembers->count;i++)
				{
					if(tempMembers->membersID[i]==userID)
					{
						done=1;
						break;
					}
				}
				if(done==0)
				{
					tempMembers=tempMembers->next;
				}
			}
			if(done==0)
			{
				adminPendingRequests* tempRequests=tempGroup->groupPendingRequests;
				adminPendingRequests* prev;
				if(tempRequests!=NULL)
				{
					while(tempRequests!=NULL && done==0)
					{
						i=0;
						prev=tempRequests;
						for(i=0;i<tempRequests->count;i++)
						{
							if(tempRequests->membersID[i]==userID)
							{
								done=1;
								break;
							}
						}
						if(done==0)
						{
							tempRequests=tempRequests->next;
						}
					}
					if(done==0)
					{
						if(tempRequests->count==MEMBERSCOUNTSIZE)
						{
							int arr[2];
							arr[0]=userID;
							arr[1]=-1;
							adminPendingRequests* newRequests=NULL;
							newRequests=createPendingRequestsSet(arr,0,1,groupID);
							tempRequests->next=newRequests;
							newRequests->next=NULL;
						}
						else
						{
							tempRequests->membersID[tempRequests->count]=userID;
							tempRequests->count++;
						}
						strcat(retArr,"REQUESTSENT:");
					}
					else
					{
						//request already sent
						strcat(retArr,"REQUESTALREADYSENT:");
					}
				}
			}
			else
			{
				//already exists in group
				strcat(retArr,"MEMBEREXISTS:");
			}
		}
	}
	else
	{
		strcat(retArr,"GROUPDOESNOTEXIST:");
	}
	return (env)->NewStringUTF(retArr);
}

//getAllMembersOfGroup(int groupID)
JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_getAllMembersOfGroup
  (JNIEnv * env, jclass, jint fromUserID, jint groupID)//seperators "|" and ":"
{
	group* tempGroup;
	tempGroup=searchGroup(groupID);
	char retArr[300],temp[6];
	retArr[0]='\0';
	if(tempGroup!=NULL)
	{
		members* tempMembers;
		tempMembers=tempGroup->groupMembers;
		if(tempMembers!=NULL)
		{
			int i,userID;
			vertex* user;
			while(tempMembers!=NULL)
			{
				i=0;
				for(i=0;i<tempMembers->count;i++)
				{
					userID=tempMembers->membersID[i];
					user=SearchUser(userID);
					if(user!=NULL)
					{
						strcat(retArr,itoa(userID,temp,10));
						strcat(retArr,":");
						strcat(retArr,user->data.Name);
						strcat(retArr,":");
						strcat(retArr,user->ImagePath);
						strcat(retArr,"|");
					}
				}
				tempMembers=tempMembers->next;
			}
		}
		else
		{
			strcat(retArr,"NOMEMBERSEXIST:");
		}
	}
	else
	{
		strcat(retArr,"GROUPDOESNOTEXIST:");
	}
	return (env)->NewStringUTF(retArr);
}


//postToGroup(int groupID,int userID,char postContent[])

JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_postToGroup
  (JNIEnv * env, jclass, jint groupID, jint fromUserID, jstring jpostContent, jlong timestamp)
{
	jboolean boolean;
	const char* pstContent=(env)->GetStringUTFChars(jpostContent,&boolean);
	char postContent[300];
	strcpy(postContent,pstContent);
	group* tempGroup;
	char retArr[50];
	retArr[0]='\0';
	tempGroup=searchGroup(groupID);
	if(tempGroup!=NULL)
	{
		post* tempPosts;
		tempPosts=createPost(fromUserID,timestamp,postContent);
		if(tempGroup->groupPosts==NULL)
		{
			tempGroup->groupPosts=tempPosts;
		}
		else
		{
			tempPosts->nextPost=tempGroup->groupPosts;
			tempGroup->groupPosts=tempPosts;
		}
		strcat(retArr,"POSTEDTOGROUPSUCCESSFULLY:");
	}
	else
	{
		strcat(retArr,"GROUPDOESNOTEXIST:");
	}
	return (env)->NewStringUTF(retArr);
}

//getAllPosts(int groupID)
JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_getAllPosts
  (JNIEnv * env, jclass, jint groupID)//seperators "|" and ":"
{
	char retArr[200];
	retArr[0]='\0';
	char temp[20];
	temp[0]='\0';
	group* tempGroup;
	tempGroup=searchGroup(groupID);
	if(tempGroup!=NULL)
	{
		post* tempPosts;
		tempPosts=tempGroup->groupPosts;
		int userID;
		vertex* user;
		if(tempPosts!=NULL)
		{
			while(tempPosts!=NULL)
			{
				userID=tempPosts->postUserID;
				user=SearchUser(userID);
				if(user!=NULL)
				{
					strcat(retArr,itoa(userID,temp,10));
					strcat(retArr,":");
					strcat(retArr,user->data.Name);
					strcat(retArr,":");
					strcat(retArr,tempPosts->postContent);
					strcat(retArr,":");
					strcat(retArr,ltoa(tempPosts->timestamp,temp,10));//!!!!!!!!ERROR!!!!!!!!!!!!
					if(tempPosts->nextPost!=NULL)
					{
						strcat(retArr,"|");
					}
				}
				tempPosts=tempPosts->nextPost;
			}
		}
		else
		{
			strcat(retArr,"NOPOSTSTOSHOW:");
		}
	}
	else
	{
		strcat(retArr,"GROUPDOESNOTEXIST:");
	}
	return (env)->NewStringUTF(retArr);
}

//getGroupInfo(int groupID)
JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_getGroupInfo
  (JNIEnv * env, jclass, jint groupID)//seperators ":" only
{
	group* tempGroup;
	tempGroup=searchGroup(groupID);
	char retArr[100],temp[10];
	retArr[0]='\0';
	temp[0]='\0';
	vertex* user;
	if(tempGroup!=NULL)
	{
		strcat(retArr,itoa(groupID,temp,10));//groupID
		strcat(retArr,":");
		strcat(retArr,tempGroup->groupName);//groupName
		strcat(retArr,":");
		strcat(retArr,itoa(tempGroup->adminID,temp,10));//adminID
		strcat(retArr,":");
		user=SearchUser(tempGroup->adminID);
		if(user!=NULL)
		{
			strcat(retArr,user->data.Name);//adminName
			strcat(retArr,":");
			members* tempMembers;
			tempMembers=tempGroup->groupMembers;
			int noOfMembers;
			if(tempMembers==NULL)
			{
				noOfMembers=0;
			}
			else
			{
				while(tempMembers!=NULL)
				{
					noOfMembers+=tempMembers->count;
					tempMembers=tempMembers->next;
				}
			}
			strcat(retArr,itoa(noOfMembers,temp,10));//noOfMembers
		}
		else
		{
			strcpy(retArr,"INVALIDGROUP:");
		}
	}
	else
	{
		strcat(retArr,"GROUPDOESNOTEXIST:");
	}
	return (env)->NewStringUTF(retArr);
}

//searchWithinGroup(int groupID,char name[])
JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_searchWithinTheGroup
  (JNIEnv * env, jclass, jint groupID, jstring jname)//seperators "|" and ":"
{
	jboolean boolean;
	const char* name=(env)->GetStringUTFChars(jname,&boolean);
	char retArr[200],temp[10];
	retArr[0]='\0';
	temp[0]='\0';
	group* tempGroup;
	tempGroup=searchGroup(groupID);
	int noOfResults=0;
	if(tempGroup!=NULL)
	{
		vertex* user;
		user=SearchUser(tempGroup->adminID);
		if(strcmp(user->data.Name,name)==0)
		{
			noOfResults++;
		}
		members* tempMembers;
		tempMembers=tempGroup->groupMembers;
		if(tempMembers!=NULL)
		{
			while(tempMembers!=NULL)
			{
				int i,userID;
				for(i=0;i<tempMembers->count;i++)
				{
					userID=tempMembers->membersID[i];
					user=SearchUser(userID);
					if(user!=NULL)
					{
						if(strcmp(user->data.Name,name)==0)
						{
							strcat(retArr,itoa(userID,temp,10));
							strcat(retArr,":");
							strcat(retArr,user->data.Name);
							strcat(retArr,"|");
							noOfResults++;
						}
					}
				}
				tempMembers=tempMembers->next;
			}
		}
		if(noOfResults==0)
		{
			strcat(retArr,"NORESULTSFOUND|");
		}
	}
	else
	{
		strcat(retArr,"GROUPDOESNOTEXIST:");
	}
	return (env)->NewStringUTF(retArr);
}

//searchForGroup(char name[])
JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_searchForGroup
  (JNIEnv * env, jclass, jstring jname)
{
	jboolean boolean;
	const char* name=(env)->GetStringUTFChars(jname,&boolean);
	char retArr[100];
	retArr[0]='\0';
	char temp[20];
	temp[0]='\0';
	group* tempGroup;
	tempGroup=Graph->groups;
	vertex* user;
	if(tempGroup!=NULL)
	{
		while(tempGroup!=NULL)
		{
			if(strcmp(tempGroup->groupName,name)==0)
			{
				strcat(retArr,itoa(tempGroup->groupID,temp,10));
				strcat(retArr,":");
				strcat(retArr,tempGroup->groupName);
				strcat(retArr,":");
				strcat(retArr,itoa(tempGroup->adminID,temp,10));
				strcat(retArr,":");
				user=SearchUser(tempGroup->adminID);
				strcat(retArr,user->data.Name);
				strcat(retArr,":");
				strcat(retArr,user->ImagePath);
				strcat(retArr,"|");
			}
			tempGroup=tempGroup->nextGroup;
		}
	}
	else
	{
		strcat(retArr,"NOGROUPSEXIST|");
	}
	return (env)->NewStringUTF(retArr);
}
//per person groupList
JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_getPerPersonGroupList
  (JNIEnv * env, jclass, jint userID)
{
	char retArr[200];
	retArr[0]='\0';
	char temp[20];
	temp[0]='\0';
	vertex* user;
	vertex* tempVertex;
	group* tempGroup;
	user=SearchUser(userID);
	int i=0,groupID;
	if(user!=NULL)
	{
		while(user->memberOfGroups[i]!=-1)
		{
			groupID=user->memberOfGroups[i];
			tempGroup=searchGroup(groupID);
			if(tempGroup!=NULL)
			{
				strcat(retArr,itoa(groupID,temp,10));
				strcat(retArr,":");
				strcat(retArr,tempGroup->groupName);
				strcat(retArr,":");
				strcat(retArr,itoa(tempGroup->adminID,temp,10));
				strcat(retArr,":");
				tempVertex=SearchUser(tempGroup->adminID);
				if(tempVertex!=NULL)
				{
					strcat(retArr,tempVertex->data.Name);
					strcat(retArr,":");
					strcat(retArr,tempVertex->ImagePath);
				}
				strcat(retArr,"|");
			}
			i++;
		}
	}
	return (env)->NewStringUTF(retArr);
}

//generate newsFeed
JNIEXPORT jstring JNICALL Java_org_SocioNet_ServerRunner_generateNewsFeed
  (JNIEnv * env, jclass, jint userID)//seperators "|" and ":"
{
	vertex* user;
	vertex* tempVertex;
	PendingFriendsStatus* tempStatus;
	long int timeStamp;
	char retArr[1000];
	retArr[0]='\0';
	char temp[20];
	user=SearchUser(userID);
	if(user!=NULL)
	{
		long int prevLoggedIn=user->prevLoggedIn;
		edge* tempEdge=NULL;
		tempEdge=user->first_edge;
		if(tempEdge!=NULL)
		{
			while(tempEdge!=NULL)
			{
				tempVertex=tempEdge->vertex_point;
				if(tempVertex->profilePicTimestamp>prevLoggedIn)
				{
					strcat(retArr,"PROFILEPICCHANGE");
					strcat(retArr,":");
					strcat(retArr,itoa(tempVertex->UserId,temp,10));
					strcat(retArr,":");
					strcat(retArr,tempVertex->data.Name);
					strcat(retArr,":");
					strcat(retArr,tempVertex->ImagePath);
					strcat(retArr,":");
					strcat(retArr,ltoa(tempVertex->profilePicTimestamp,temp,10));
					strcat(retArr,"|");
				}
				if(tempVertex!=NULL)
					timeStamp=tempVertex->statusTimestamp;
				if(tempStatus!=NULL)
				{
					if(timeStamp>prevLoggedIn)
					{
						strcat(retArr,"STATUSUPDATE");
						strcat(retArr,":");
						strcat(retArr,itoa(tempVertex->UserId,temp,10));
						strcat(retArr,":");
						strcat(retArr,tempVertex->data.Name);
						strcat(retArr,":");
						strcat(retArr,tempVertex->ImagePath);
						strcat(retArr,":");
						strcat(retArr,ltoa(timeStamp,temp,10));
						strcat(retArr,":");
						strcat(retArr,tempStatus->Status);
						strcat(retArr,"|");
					}
				}
				tempEdge=tempEdge->next_edge;
			}
		}
	}
	return (env)->NewStringUTF(retArr);
}
