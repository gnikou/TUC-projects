#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pcap.h>
#include <netinet/if_ether.h>
#include <net/ethernet.h>
#include <netinet/udp.h>
#include <netinet/tcp.h>
#include <netinet/ip.h>

int tcp_count = 0;
int udp_count = 0;
int pack_count = 0;
int tcp_bytes = 0;
int udp_bytes = 0;

void usage(void);
void packet_process(char *);
void packet_handler(u_char *, const struct pcap_pkthdr *, const u_char *);
void TCP_analysis(struct iphdr *, const u_char *);
void UDP_analysis(struct iphdr *, const u_char *);
void print_statistics();

void usage()
{
    printf(
        "\n"
        "Usage:\n"
        "\tNetwork monitor \n\n"
        "Options:\n"
        "-r <filename>, Read and decode packets from "
        "file <filename> and print stats for detected packets\n"
        "-h, Help message\n\n");

    exit(1);
}

int main(int argc, char *argv[])
{

    int opt;
    char *input_file; /* path to the input file */
    input_file = NULL;
    while ((opt = getopt(argc, argv, "hr:")) != -1)
    {
        switch (opt)
        {
        case 'h':
            usage();
            break;
        case 'r':
            input_file = strdup(optarg);
            packet_process(input_file);
            print_statistics();
            break;
        default:
            printf("Error! Wrong flag\n");
        }
    }

    if (argc == 1)
        printf("Error! Not enough arguments\n");

    return 0;
}


/* open file with pcap_open_offline and read each packet with pcap_loop*/

void packet_process(char *pcap_file)
{
    char errbuf[PCAP_ERRBUF_SIZE];
    pcap_t *handle;

    handle = pcap_open_offline(pcap_file, errbuf);

    pcap_loop(handle, 0, packet_handler, NULL);
}


/* called by pcal_loop it handles the next packet in file */

void packet_handler(u_char *args, const struct pcap_pkthdr *header,
                    const u_char *packet)
{
    struct iphdr *ip_header;

    ip_header = (struct iphdr *)(packet + sizeof(struct ethhdr));

    if (ip_header->protocol == IPPROTO_TCP)
    {
        TCP_analysis(ip_header, packet);
    }
    else if (ip_header->protocol == IPPROTO_UDP)
    {
        UDP_analysis(ip_header, packet);
    }
    pack_count++;
}


/* Analysis of a TCP packet */

void TCP_analysis(struct iphdr *ip_header, const u_char *packet)
{
    struct tcphdr *tcp_header;
    int data_len;

    tcp_header = (struct tcphdr *)(packet + sizeof(struct ethhdr) + ip_header->ihl * 4);

    data_len = ntohs(ip_header->tot_len) - ip_header->ihl * 4 - sizeof(struct tcphdr);

    printf("==========================================\n");
    printf("[TCP]\n");
    printf("Source IPv4 address: %s\n", inet_ntoa(*(struct in_addr *)&ip_header->saddr));
    printf("Dest IPv4 address: %s\n", inet_ntoa(*(struct in_addr *)&ip_header->daddr));
    printf("Source Port: %d\n", ntohs(tcp_header->source));
    printf("Dest Port: %d\n", ntohs(tcp_header->dest));
    printf("Header Len : %d bytes\n", (ip_header->ihl * 4));
    printf("Payload Len : %d bytes\n", data_len);
    tcp_bytes += ntohs(ip_header->tot_len);
    tcp_count++;
}


/* Analysis of a UDP packet*/

void UDP_analysis(struct iphdr *ip_header, const u_char *packet)
{
    struct udphdr *udp_header;
    int data_len;

    udp_header = (struct udphdr *)(packet + sizeof(struct ethhdr) + ip_header->ihl * 4);
    data_len = ntohs(ip_header->tot_len) - ip_header->ihl * 4 - sizeof(struct udphdr);

    printf("==========================================\n");
    printf("[UDP]\n");
    printf("Source IPv4 address: %s\n", inet_ntoa(*(struct in_addr *)&ip_header->saddr));
    printf("Dest IPv4 address: %s\n", inet_ntoa(*(struct in_addr *)&ip_header->daddr));
    printf("Source Port: %d\n", ntohs(udp_header->source));
    printf("Dest Port: %d\n", ntohs(udp_header->dest));
    printf("Header Len : %d bytes\n", (ip_header->ihl * 4));
    printf("Payload Len : %d bytes\n", data_len);
    udp_bytes += ntohs(ip_header->tot_len);
    udp_count++;
}


/* Print stats before the exit of the program */

void print_statistics()
{
    printf("------------------------------------------\nStatistics:\n");
    printf("Total number of packets received: %d\n", pack_count);
    printf("Total number of TCP packets received: %d\n", tcp_count);
    printf("Total number of UDP packets received: %d\n", udp_count);
    printf("Total bytes of TCP packets received: %d bytes\n", tcp_bytes);
    printf("Total bytes of UDP packets received: %d bytes\n", udp_bytes);
}
