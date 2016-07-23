package com.kz.pipeCutter.BBB.commands;

import com.google.protobuf.InvalidProtocolBufferException;

import pb.Message.Container;
import pb.Types.ContainerType;

public class UnHomeAllAxis extends MachineTalkCommand {
	public UnHomeAllAxis() {
	}

	@Override
	public Container prepareContainer() throws Exception {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < 5; i++) {
			pb.Message.Container.Builder builder = Container.newBuilder();
			pb.Status.EmcCommandParameters emcCommandParameter = pb.Status.EmcCommandParameters.newBuilder().setIndex(i)
					.build();
			builder.setType(ContainerType.MT_EMC_AXIS_UNHOME);
			builder.setEmcCommandParams(emcCommandParameter);
			builder.setTicket(ticket++);
			Container container = builder.build();
			socket.send(container.toByteArray());
			try {
				parseAndOutput();
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
